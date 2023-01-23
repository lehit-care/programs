package com.lehit.programs.controller;

import com.lehit.programs.model.Program;
import com.lehit.programs.model.payload.ProgramSequence;
import com.lehit.programs.repository.ProgramRepository;
import com.lehit.programs.service.ActionItemsService;
import com.lehit.programs.service.TasksService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
//todo basePath
@Slf4j
@RequiredArgsConstructor
public class ProgramContentController {
    private final TasksService tasksService;
    private final ActionItemsService itemsService;
    private final ProgramRepository programRepository;

    //    Programs
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/programs/{author}/create")
    public Program createProgram(@PathVariable UUID author, @RequestBody Program program) {
        program.setAuthor(author);
        return programRepository.save(program);
    }

//    todo
//    @ResponseStatus(HttpStatus.NO_CONTENT)
//    @DeleteMapping("/programs/{id}")
//    public void deleteProgram(@PathVariable UUID id) {
//        tasksService.deleteTask(id);
//    }

//    todo Update Program


    //    Tasks
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/tasks/{id}")
    public void deleteTask(@PathVariable UUID id) {
        tasksService.deleteTask(id);
    }

//    @ResponseStatus(HttpStatus.NO_CONTENT)
//    @PostMapping("/tasks/{id}/archive")
//    public void archiveTask(@PathVariable UUID id) {
//        tasksService.archiveTask(id);
//    }


    @PostMapping("/tasks/{programId}/shuffle")
    public ProgramSequence shuffleTasks(@PathVariable UUID programId, @RequestBody ProgramSequence sequence) {
        tasksService.shuffleTasks(programId, sequence.sequences());
        return sequence;
    }

    @PostMapping("/items/shuffle/{taskId}")
    public ProgramSequence shuffleItems(@PathVariable UUID taskId, @RequestBody ProgramSequence sequence) {
        itemsService.shuffleItems(taskId, sequence.sequences());
        return sequence;
    }

    //    ActionItems
    @DeleteMapping("/items/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteItem(@PathVariable UUID id) {
        itemsService.removeActionItem(id);
    }


    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping("/items/{id}/archive")
    public void archiveItem(@PathVariable UUID id) {
        itemsService.archiveItem(id);
    }

}
