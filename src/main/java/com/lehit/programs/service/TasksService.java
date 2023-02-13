package com.lehit.programs.service;

import com.lehit.programs.model.Task;
import com.lehit.programs.model.payload.ProgramSequence;
import com.lehit.programs.model.projection.TaskWithItemsProjection;
import com.lehit.programs.repository.ProgramRepository;
import com.lehit.programs.repository.TaskRepository;
import com.lehit.programs.service.utils.BeanUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.util.Asserts;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class TasksService {
    private final TaskRepository taskRepository;
    private final ProgramRepository programRepository;
    private final BeanUtils beanUtils;
    //    private final MultimediaClient multimediaClient;



    List<UUID> getIdsByProgramId(UUID programId){
        return taskRepository.selectIdsByProgramId(programId);
    }

    @Transactional
    public Task save(UUID authorId, Task task){
        Asserts.check(authorId.equals(programRepository.findById(task.getProgramId()).orElseThrow().getAuthorId()), "Not allowed.");
        return taskRepository.save(task);
    }

    @Transactional
    public Task updateTask(UUID authorId, UUID taskId, Map<String, Object> fields) {
        var task = taskRepository.selectFullTask(taskId).orElseThrow();
        Asserts.check(authorId.equals(task.getProgram().getAuthorId()), "Only Author can modify the Program.");

        beanUtils.updateFields(fields, task);
        task.setId(taskId);
        return task;
    }


    @Transactional
    public void deleteTask(UUID authorId, UUID taskId){
        var task = taskRepository.selectFullTask(taskId).orElseThrow();

        Asserts.check(authorId.equals(task.getProgram().getAuthorId()), "Not allowed.");

        taskRepository.delete(task);

        var multimediaList = task.getActionItems().stream()
                .filter(ai -> ai.getInformationItem() != null)
                .map(item -> item.getInformationItem().getMediaFileUrl())
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        if(task.getAvatarUrl() != null)
            multimediaList.add(task.getAvatarUrl());

//        if(!multimediaList.isEmpty())
//            multimediaClient.deleteMultimediaList(multimediaList);
    }

// todo change logic
//    @Transactional
//    public void archiveTask(UUID taskId){
//        var task = taskRepository.findById(taskId).orElseThrow();
//
//        var programId= task.getProgramId();
//
//        taskExecutionRepository.updateTaskExecutionsLCS(position, CANCELLED);
//        syncTasksPosition(programId, position);
//    }


    public Slice<TaskWithItemsProjection> getTasksByProgram(UUID programId, Pageable pageable){
        return taskRepository.selectTasksWithItemsByProgramId(programId, pageable);
    }


    @Transactional
    public List<ProgramSequence.ExecutableEntitySequence> shuffleTasks(UUID programId, List<ProgramSequence.ExecutableEntitySequence> sequenceList){
        Map<UUID, Integer> sequenceMap =  sequenceList.stream()
                .collect(Collectors.toMap(ProgramSequence.ExecutableEntitySequence::id, ProgramSequence.ExecutableEntitySequence::position));

        var tasks = taskRepository.findAllById(sequenceMap.keySet());
        tasks.forEach(task -> task.setPosition(sequenceMap.get(task.getId())));

        return sequenceList;
    }

}
