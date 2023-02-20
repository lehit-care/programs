package com.lehit.programs.controller;

import com.lehit.programs.model.ActionItem;
import com.lehit.programs.model.Program;
import com.lehit.programs.model.Task;
import com.lehit.programs.model.payload.ProgramSequence;
import com.lehit.programs.model.projection.ProgramWithTasksProjection;
import com.lehit.programs.model.projection.TaskWithItemsProjection;
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
import java.util.Optional;
import java.util.UUID;

import static com.lehit.programs.model.enums.ContentVisibilityStatus.ARCHIVED;
import static com.lehit.programs.model.enums.ContentVisibilityStatus.PUBLISHED;

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
        program.setAuthorId(authorId);
        return programsService.saveNewProgram(program);
    }

    @PatchMapping("/author/{authorId}/programs/{programId}")
    public Program updateProgramBasicData(@PathVariable UUID authorId, @PathVariable UUID programId,  @RequestBody Map<String, Object> programPayload) {
        return programsService.updateProgramData(authorId, programId, programPayload);
    }

    @PostMapping("/author/{authorId}/programs/{programId}/publish")
    public Program publishProgram(@PathVariable UUID authorId, @PathVariable UUID programId) {
        return programsService.changeProgramVisibilityStatus(authorId, programId, PUBLISHED);
    }

    @PostMapping("/author/{authorId}/programs/{programId}/archive")
    public Program archiveProgram(@PathVariable UUID authorId, @PathVariable UUID programId) {
        return programsService.changeProgramVisibilityStatus(authorId, programId, ARCHIVED);
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

    @GetMapping("/author/{authorId}/programs/{id}")
    public Optional<ProgramWithTasksProjection> getProgramsByAuthor(@PathVariable UUID id){
        return programsService.findProgramWithTasks(id);
    }

    @GetMapping("/author/{authorId}/programs/{id}/tasks")
    public Slice<TaskWithItemsProjection> getProgramTasks(@PathVariable UUID id, @ParameterObject Pageable pageable){
        return tasksService.getTasksByProgram(id, pageable);
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
    public Task updateTaskBasicData(@PathVariable UUID authorId, @PathVariable UUID id,  @RequestBody Map<String, Object> payload) {
        return tasksService.updateTask(authorId, id, payload);
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

    @GetMapping("/programs/{id}/tasks")
    public Slice<TaskWithItemsProjection> getProgramTasksSlice (@PathVariable UUID programId, @ParameterObject Pageable pageable) {
       return tasksService.getTasksByProgram(programId, pageable);
    }

    //    ActionItems
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/author/{authorId}/items")
    public ActionItem saveActionItem(@PathVariable UUID authorId, @Valid @RequestBody ActionItem ai) {
        return itemsService.save(authorId, ai);
    }


    @DeleteMapping("/author/{authorId}/items/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteItem(@PathVariable UUID authorId, @PathVariable UUID id) {
        itemsService.removeActionItem(authorId, id);
    }

    @PatchMapping("/author/{authorId}/items/{aiId}")
    public ActionItem updateAIBasicData(@PathVariable UUID authorId, @PathVariable UUID aiId,  @RequestBody Map<String, Object> payload) {
        return itemsService.updateItem(authorId, aiId, payload);
    }

    @GetMapping("/task/{taskId}/items")
    public Slice<ActionItem> getAIsByTask(@PathVariable UUID taskId, @ParameterObject Pageable pageable){
        return itemsService.getByTaskId(taskId, pageable);
    }


    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping("/items/{id}/archive")
    public void archiveItem(@PathVariable UUID id) {
        itemsService.archiveItem(id);
    }

}
