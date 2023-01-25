package com.lehit.programs.service;

import com.lehit.common.enums.ExecutionStatus;
import com.lehit.programs.model.ItemExecution;
import com.lehit.programs.model.ProgramExecution;
import com.lehit.programs.model.TaskExecution;
import com.lehit.programs.model.payload.ExecutedItemRequest;
import com.lehit.programs.model.projection.TaskExecutionWithItemsProjection;
import com.lehit.programs.repository.ItemExecutionRepository;
import com.lehit.programs.repository.ProgramExecutionRepository;
import com.lehit.programs.repository.TaskExecutionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.util.Asserts;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.lehit.common.enums.ExecutionStatus.STARTED;

@RequiredArgsConstructor
@Slf4j
@Service
@Transactional(readOnly = true)
public class ExecutionProgressService {
    private final TasksService tasksService;
    private final ActionItemsService actionItemsService;
    private final TaskExecutionRepository taskExecutionRepository;
    private final ProgramExecutionRepository programExecutionRepository;
    private final ItemExecutionRepository itemExecutionRepository;


    @Transactional
    public ProgramExecution assignProgram(UUID userId, UUID programId){
        log.debug("assign program {} for user {}", programId, userId);

        var programExecution = programExecutionRepository.save(ProgramExecution.builder()
                .programId(programId)
                .lifecycleStatus(STARTED)
                .startedAt(LocalDateTime.now())
                .userId(userId)
                .build());

        var plannedTaskExecutions = tasksService.getIdsByProgramId(programId).stream()
                .map(taskId -> TaskExecution.builder()
                        .programExecutionId(programExecution.getId())
                        .userId(userId)
                        .taskId(taskId)
                        .build())
                .collect(Collectors.toList());

        taskExecutionRepository.saveAll(plannedTaskExecutions);
        return programExecution;
    }


    @Transactional
    public TaskExecution startTaskExecution(UUID userId, UUID taskId){
        var exe = taskExecutionRepository.findByUserIdAndTaskId(userId, taskId);

        return exe.map(existingExecution -> {
            existingExecution.setStartedAt(LocalDateTime.now());
            existingExecution.setLifecycleStatus(STARTED);
            var plannedItemExecutions = actionItemsService.getIdsByTaskId(taskId).stream()
                    .map(aiId -> ItemExecution.builder()
                            .userId(userId)
                            .taskExecutionId(existingExecution.getId())
                            .itemId(aiId)
                            .build())
                    .collect(Collectors.toList());
            itemExecutionRepository.saveAll(plannedItemExecutions);

            return exe;
        }).get().orElseThrow();
    }



    @Transactional
    public TaskExecution finishTaskExecution(UUID userId, UUID executionId){
        TaskExecution execution = taskExecutionRepository.findById(executionId).orElseThrow();
        Asserts.check(userId.equals(execution.getUserId()), "Not allowed.");

        execution.setFinishedAt(LocalDateTime.now());
        execution.setLifecycleStatus(ExecutionStatus.FINISHED);
        return execution;
    }

    @Transactional
    public ItemExecution executeItem(ExecutedItemRequest rel, UUID userId, UUID itemId){
        var plannedExe = itemExecutionRepository.findByTaskExecutionIdAndItemId(rel.taskExecutionId(), itemId)
                .orElseThrow();
//        todo assert User

        plannedExe.setLifecycleStatus(ExecutionStatus.FINISHED);
        plannedExe.setItemType(rel.itemType());
        return plannedExe;
    }

    public ProgramExecution getActiveProgramExecutionData(UUID userId){
        return programExecutionRepository.findByUserIdAndLifecycleStatus(userId, STARTED);
    }

    public TaskExecutionWithItemsProjection getTaskExecutionData(UUID userId, UUID taskExecutionId){
        var execution = taskExecutionRepository.selectByExeId(taskExecutionId).orElseThrow();
        Asserts.check(userId.equals(execution.getUserId()), "Not allowed.");

        return execution;
    }

}
