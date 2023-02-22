package com.lehit.programs.service;

import com.lehit.common.enums.ExecutionStatus;
import com.lehit.programs.model.ItemExecution;
import com.lehit.programs.model.ProgramExecution;
import com.lehit.programs.model.TaskExecution;
import com.lehit.programs.model.payload.ExecutedItemRequest;
import com.lehit.programs.model.projection.ProgramExecutionBasicProjection;
import com.lehit.programs.model.projection.ProgramExecutionWithTaskExecutions;
import com.lehit.programs.model.projection.TaskExecutionWithItemsProjection;
import com.lehit.programs.repository.ItemExecutionRepository;
import com.lehit.programs.repository.ProgramExecutionRepository;
import com.lehit.programs.repository.TaskExecutionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.util.Asserts;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.lehit.common.enums.ExecutionStatus.*;

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
                        .lifecycleStatus(NEW)
                        .build())
                .collect(Collectors.toList());

        taskExecutionRepository.saveAll(plannedTaskExecutions);
        return programExecution;
    }


    @Transactional
    public ProgramExecution finishProgramExecution(UUID userId, UUID executionId){
        return changeProgramExecutionLCS(userId, executionId, FINISHED);
    }

    @Transactional
    public ProgramExecution cancelProgramExecution(UUID userId, UUID executionId){
        return changeProgramExecutionLCS(userId, executionId, CANCELLED);
    }

    private ProgramExecution changeProgramExecutionLCS(UUID userId, UUID executionId, ExecutionStatus lcs){
        ProgramExecution execution = programExecutionRepository.findById(executionId).orElseThrow();
        Asserts.check(userId.equals(execution.getUserId()), "Not allowed.");

        execution.setFinishedAt(LocalDateTime.now());
        execution.setLifecycleStatus(lcs);
        return execution;
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
                            .lifecycleStatus(NEW)
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
        execution.setLifecycleStatus(FINISHED);
        return execution;
    }

    @Transactional
    public ItemExecution executeItem(ExecutedItemRequest rel, UUID userId, UUID itemId){
        var plannedExe = itemExecutionRepository.findByTaskExecutionIdAndItemId(rel.taskExecutionId(), itemId)
                .orElseThrow();

        Asserts.check(userId.equals(plannedExe.getUserId()), "Not allowed.");

        plannedExe.setLifecycleStatus(FINISHED);
        plannedExe.setItemType(rel.itemType());
        return plannedExe;
    }


    //    using direct query limitation limits the number of tasks executions as it fetches the join table
    public Optional<ProgramExecution> getActiveProgramExecutionData(UUID userId){
        return programExecutionRepository
                .findByUserIdAndLifecycleStatus(userId, STARTED,  PageRequest.of(0, 1, Sort.by(Sort.Direction.DESC, "startedAt")))
                .stream().findFirst();
    }

    public Optional<ProgramExecution> getActiveProgramExecutionData1(UUID userId){
        return programExecutionRepository
                .findProgramExecutionsWithTaskExecutionsWithTasks(userId, 1)
                .stream().findFirst();
    }


    public Slice<ProgramExecution> getProgramExecutionData(UUID userId, ExecutionStatus lcs, Pageable pageable){
        return programExecutionRepository
                .findByUserIdAndLifecycleStatus(userId, lcs, pageable);
    }

    public Optional<ProgramExecution> getByClientAndProgram(UUID userId, UUID programId){
        return programExecutionRepository.findByUserIdAndProgramId(userId, programId);
    }

    public Slice<ProgramExecutionBasicProjection> getByClientAndLCS(UUID userId, ExecutionStatus lcs){
        return programExecutionRepository.findByUserIdAndLifecycleStatus(userId, lcs);
    }

    public Slice<ProgramExecutionBasicProjection> getByClient(UUID userId){
        return programExecutionRepository.findByUserId(userId);
    }

    public TaskExecutionWithItemsProjection getTaskExecutionData(UUID userId, UUID taskExecutionId){
        var execution = taskExecutionRepository.selectByExeId(taskExecutionId).orElseThrow();
        Asserts.check(userId.equals(execution.getUserId()), "Not allowed.");

        return execution;
    }

}
