package com.lehit.programs.service;

import com.lehit.common.dto.UserProgramData;
import com.lehit.common.enums.ExecutionStatus;
import com.lehit.programs.model.TaskExecution;
import com.lehit.programs.model.payload.ProgramExecutionItem;
import com.lehit.programs.model.payload.ProgramTasksProgress;
import com.lehit.programs.repository.TaskExecutionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.util.Asserts;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
@Service
public class ExecutionProgressService {
    private final TasksService tasksService;
    private final TaskExecutionRepository taskExecutionRepository;
    private final UserService userService;



    @Transactional
    public TaskExecution startTaskExecution(UUID userId, int taskPosition){
        Asserts.check(!taskExecutionRepository.existsByUserIdAndLifecycleStatus(userId, ExecutionStatus.STARTED), "There's an unfinished Task.");
        return persistTaskExecution(userService.getUserData(userId), taskPosition);
    }

    private TaskExecution persistTaskExecution(UserProgramData userData, int taskPosition) {
        return taskExecutionRepository.save(TaskExecution.builder()
                .userId(userData.getId())
                .taskPosition(taskPosition)
                .lifecycleStatus(ExecutionStatus.STARTED)
                .programId(userData.getProgramId())
                .startedAt(LocalDateTime.now())
                .build());
    }


    @CacheEvict(value= "WebTasksExecutionsByUser", key = "#userId")
    @Transactional
    public ProgramTasksProgress finishTaskExecution(UUID userId, UUID executionId){
        TaskExecution execution = taskExecutionRepository.findById(executionId).orElseThrow();
        Asserts.check(userId.equals(execution.getUserId()), "Not allowed.");

        execution.setFinishedAt(LocalDateTime.now());
        execution.setLifecycleStatus(ExecutionStatus.FINISHED);

        return new ProgramTasksProgress(tasksService.getByProgramPosition(execution.getProgramId(), execution.getTaskPosition()+1)
                .orElse(null), tasksService.getProgramTasksCount(execution.getProgramId()));
    }


    @CacheEvict(value= "WebTasksExecutionsByUser", key = "#userId")
    @Transactional
    public int cancelUserExecutions(UUID userId){
        log.debug("cancel user executions {}", userId);
        return taskExecutionRepository.updateUserExecutionsLCS(userId, ExecutionStatus.CANCELLED);
    }




    @CacheEvict(value= "WebTasksExecutionsByUser", key = "#userId")
    @Transactional
    public void removeUserData(UUID userId){
        taskExecutionRepository.deleteByUserId(userId);
    }
}
