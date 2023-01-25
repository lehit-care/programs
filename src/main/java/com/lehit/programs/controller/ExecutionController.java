package com.lehit.programs.controller;

import com.lehit.programs.model.ItemExecution;
import com.lehit.programs.model.ProgramExecution;
import com.lehit.programs.model.TaskExecution;
import com.lehit.programs.model.payload.ExecutedItemRequest;
import com.lehit.programs.model.projection.TaskExecutionWithItemsProjection;
import com.lehit.programs.service.ActionItemsService;
import com.lehit.programs.service.ExecutionProgressService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class ExecutionController {
    private final ActionItemsService itemsService;
    private final ExecutionProgressService executionService;


    @PostMapping("/executions/{clientId}/start-program/{programId}")
    public ProgramExecution assignProgram(@PathVariable UUID clientId, @PathVariable UUID programId) {
       return executionService.assignProgram(clientId, programId);
    }

    @PostMapping("/executions/{clientId}/start-task/{taskId}")
    public TaskExecution startTask(@PathVariable UUID clientId, @PathVariable UUID taskId) {
        return executionService.startTaskExecution(clientId, taskId);
    }


    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping("/executions/{clientId}/execute-item/{id}")
    public void executeItem(@PathVariable UUID clientId, @PathVariable UUID id, @RequestBody ExecutedItemRequest executedItemRequest) {
        ItemExecution executedItem = executionService.executeItem(executedItemRequest, clientId, id);
        CompletableFuture.supplyAsync(() -> itemsService.emitEvent(executedItem));
    }


    @PostMapping("/executions/{clientId}/finish-task/{taskExecutionId}")
    public TaskExecution finishTask(@PathVariable UUID clientId, @PathVariable UUID taskExecutionId) {
        return executionService.finishTaskExecution(clientId, taskExecutionId);
    }

    @GetMapping("/executions/{clientId}/current-program")
    public ProgramExecution getCurrentProgramData(@PathVariable UUID clientId) {
        return executionService.getActiveProgramExecutionData(clientId);
    }

    @GetMapping("/executions/{clientId}/task-exe/{taskExecutionId}")
    public TaskExecutionWithItemsProjection getCurrentProgramData(@PathVariable UUID clientId, @PathVariable UUID taskExecutionId) {
        return executionService.getTaskExecutionData(clientId, taskExecutionId);
    }

}
