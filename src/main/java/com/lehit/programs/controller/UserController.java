package com.lehit.programs.controller;

import com.lehit.programs.model.ExecutedItem;
import com.lehit.programs.model.TaskExecution;
import com.lehit.programs.model.payload.ExecutedItemRequest;
import com.lehit.programs.model.payload.ProgramTasksProgress;
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
//todo base path
public class UserController {
    private final ActionItemsService itemsService;
    private final ExecutionProgressService executionService;


// todo taskID
    @PostMapping("/commands/client/{clientId}/start-task/{taskPosition}")
    public TaskExecution startTask(@PathVariable UUID clientId, @PathVariable int taskPosition) {
        return executionService.startTaskExecution(clientId, taskPosition);
    }


    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping("/commands/client/{clientId}/execute-item/{id}")
    public void executeItem(@PathVariable UUID clientId, @PathVariable UUID id, @RequestBody ExecutedItemRequest executedItemRequest) {
        ExecutedItem executedItem = itemsService.executeItem(executedItemRequest, clientId, id);
        CompletableFuture.supplyAsync(() -> itemsService.emitEvent(executedItem));
    }


    @PostMapping("/commands/client/{clientId}/finish-task/{taskExecutionId}")
    public ProgramTasksProgress finishTask(@PathVariable UUID clientId, @PathVariable UUID taskExecutionId) {
        return executionService.finishTaskExecution(clientId, taskExecutionId);
    }

}
