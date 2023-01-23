package com.lehit.programs.service;

import com.lehit.programs.client.feign.MultimediaClient;
import com.lehit.programs.model.Task;
import com.lehit.programs.model.payload.ProgramSequence;
import com.lehit.programs.repository.TaskExecutionRepository;
import com.lehit.programs.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class TasksService {
    private final TaskRepository taskRepository;
    private final TaskExecutionRepository taskExecutionRepository;
    private final MultimediaClient multimediaClient;


    public Optional<Task> getByProgramPosition(UUID program,  int position){
        return taskRepository.findByProgramIdAndPosition(program, position);
    }


    @Transactional
    public void removeUserData(UUID userId){
        taskExecutionRepository.deleteByUserId(userId);
    }

    @Transactional
    public void deleteTask(UUID taskId){
        var task = taskRepository.selectFullTask(taskId).orElseThrow();

        var multimediaList = task.getActionItems().stream()
                .filter(ai -> ai.getInformationItem() != null)
                .map(item -> item.getInformationItem().getMediaFileUrl())
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        if(task.getAvatarUrl() != null)
            multimediaList.add(task.getAvatarUrl());

        taskRepository.delete(task);

        if(!multimediaList.isEmpty())
            multimediaClient.deleteMultimediaList(multimediaList);

        syncTasksPosition(task.getProgramId(), task.getPosition());
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


    public void syncTasksPosition(UUID programId,  int position){
        List<Task> tasks = taskRepository.findByProgramIdOrderByPositionAsc(programId, Task.class);
        if(position > tasks.size())
            return;

        for(int pos = position; pos<=tasks.size(); pos++){
            tasks.get(pos-1).setPosition(pos);
        }
    }

    public long getProgramTasksCount(UUID programId){
        return taskRepository.countByProgramId(programId);
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
