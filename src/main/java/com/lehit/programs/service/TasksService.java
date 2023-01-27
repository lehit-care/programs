package com.lehit.programs.service;

import com.lehit.programs.model.Task;
import com.lehit.programs.model.payload.ProgramSequence;
import com.lehit.programs.repository.TaskExecutionRepository;
import com.lehit.programs.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final TaskExecutionRepository taskExecutionRepository;
//    private final MultimediaClient multimediaClient;



    List<UUID> getIdsByProgramId(UUID programId){
        return taskRepository.selectIdsByProgramId(programId);
    }

    @Transactional
    public Task save(Task task){
        return taskRepository.save(task);
    }


//    todo check author
    @Transactional
    public void deleteTask(UUID taskId){
        var task = taskRepository.selectFullTask(taskId).orElseThrow();
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





    @Transactional
    public List<ProgramSequence.ExecutableEntitySequence> shuffleTasks(UUID programId, List<ProgramSequence.ExecutableEntitySequence> sequenceList){
        Map<UUID, Integer> sequenceMap =  sequenceList.stream()
                .collect(Collectors.toMap(ProgramSequence.ExecutableEntitySequence::id, ProgramSequence.ExecutableEntitySequence::position));

        var tasks = taskRepository.findAllById(sequenceMap.keySet());
        tasks.forEach(task -> task.setPosition(sequenceMap.get(task.getId())));

        return sequenceList;
    }

}
