package com.lehit.programs.controller;

import com.lehit.programs.model.ActionItem;
import com.lehit.programs.model.Program;
import com.lehit.programs.model.Task;
import com.lehit.programs.model.payload.ProgramSequence;
import com.lehit.programs.service.ActionItemsService;
import com.lehit.programs.service.ProgramsService;
import com.lehit.programs.service.TasksService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@Slf4j
@RequiredArgsConstructor
public class ProgramContentController {
    private final TasksService tasksService;
    private final ActionItemsService itemsService;
    private final ProgramsService programsService;

    //    Programs
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/author/{authorId}/programs")
    public Program createProgram(@PathVariable UUID authorId, @Valid @RequestBody Program program) {
        program.setAuthor(authorId);
        return programsService.saveProgram(program);
    }

    @PatchMapping("/author/{authorId}/programs/{programId}")
    public Program updateProgramBasicData(@PathVariable UUID authorId, @PathVariable UUID programId,  @RequestBody Map<String, Object> programPayload) {
        return programsService.updateProgramData(authorId, programId, programPayload);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/author/{authorId}/programs/{id}")
    public void deleteProgram(@PathVariable UUID authorId, @PathVariable UUID id) {
        programsService.deleteProgram(authorId, id);
    }

    @GetMapping("/author/{authorId}/programs")
    public Slice<Program> getProgramsByAuthor(@PathVariable UUID authorId, @ParameterObject Pageable pageable){
        return programsService.findByAuthor(authorId, pageable);
    }

    @GetMapping("/programs")
    public Slice<Program> getAllPrograms(@ParameterObject Pageable pageable){
        return programsService.findAll(pageable);
    }


    //    Tasks
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/author/{authorId}/tasks")
    public Task saveTask(@PathVariable UUID authorId, @Valid @RequestBody Task task) {
        return tasksService.save(authorId, task);
    }


    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/author/{authorId}/tasks/{id}")
    public void deleteTask(@PathVariable UUID authorId, @PathVariable UUID id) {
        tasksService.deleteTask(authorId, id);
    }

    @PatchMapping("/author/{authorId}/tasks/{id}")
    public Task updateTaskBasicData(@PathVariable UUID authorId, @PathVariable UUID programId,  @RequestBody Map<String, Object> payload) {
        return tasksService.updateTask(authorId, programId, payload);
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

    @PostMapping("/items/{taskId}/shuffle")
    public ProgramSequence shuffleItems(@PathVariable UUID taskId, @RequestBody ProgramSequence sequence) {
        itemsService.shuffleItems(taskId, sequence.sequences());
        return sequence;
    }

    //    ActionItems
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/items")
    //    todo ("/author/{authorId}/items}")
    public ActionItem saveActionItem(@Valid @RequestBody ActionItem ai) {
        return itemsService.save(ai);
    }


    @DeleteMapping("/author/{authorId}/items/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteItem(@PathVariable UUID authorId, @PathVariable UUID id) {
        itemsService.removeActionItem(authorId, id);
    }

    @PatchMapping("/author/{authorId}/items/{id}")
    public ActionItem updateAIBasicData(@PathVariable UUID authorId, @PathVariable UUID aiId,  @RequestBody Map<String, Object> payload) {
        return itemsService.updateItem(authorId, aiId, payload);
    }


    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping("/items/{id}/archive")
    public void archiveItem(@PathVariable UUID id) {
        itemsService.archiveItem(id);
    }

}
