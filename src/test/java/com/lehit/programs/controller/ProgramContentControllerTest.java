package com.lehit.programs.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lehit.programs.data.TestDataGenerator;
import com.lehit.programs.data.TestDataTx;
import com.lehit.programs.model.enums.ActionItemType;
import com.lehit.programs.service.ExecutionProgressService;
import com.lehit.programs.service.TasksService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Map;
import java.util.UUID;

import static com.lehit.programs.model.enums.ContentVisibilityStatus.ARCHIVED;
import static com.lehit.programs.model.enums.ContentVisibilityStatus.PUBLISHED;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Slf4j
class ProgramContentControllerTest {
    @Autowired
    private TestDataTx testDataTx;
    @Autowired
    private TestDataGenerator testDataGenerator;
    @Autowired
    private TasksService tasksService;


    @Autowired
    private ExecutionProgressService progressService;

    @Autowired
    protected MockMvc mockMvc;



    protected static final String CONTROLLER_URL_ROOT_PREFIX = "/api/v1/";


    @Test
    void getProgramsByAuthor() throws Exception {

        var author = testDataTx.saveAuthor(testDataGenerator.generateAuthor());

        testDataTx.saveProgram(testDataGenerator.generateProgram(author.getId()));
        testDataTx.saveProgram(testDataGenerator.generateProgram(author.getId()));
        testDataTx.saveProgram(testDataGenerator.generateProgram(author.getId()));


        this.mockMvc.perform(get(CONTROLLER_URL_ROOT_PREFIX + "/author/{authorId}/programs", author.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .queryParam("page", "0"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.pageable.pageSize").value("20"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.numberOfElements").value("3"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.last").value(true));
    }


    @Test
    void getProgramByAuthor() throws Exception {

        var author = testDataTx.saveAuthor(testDataGenerator.generateAuthor());

        var program = testDataTx.saveProgram(testDataGenerator.generateProgram(author.getId()));
        var task1 = testDataTx.saveTask(testDataGenerator.generateTask(program.getId(), 1));
        var task2 = testDataTx.saveTask(testDataGenerator.generateTask(program.getId(), 2));
        var task3 = testDataTx.saveTask(testDataGenerator.generateTask(program.getId(), 3));




        this.mockMvc.perform(get(CONTROLLER_URL_ROOT_PREFIX + "/author/{authorId}/programs/{id}", author.getId(), program.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(program.getId().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.tasks[0].id").value(task1.getId().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.tasks[1].id").value(task2.getId().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.tasks[2].id").value(task3.getId().toString()));
    }

    @Test
    void getItemsByTask() throws Exception {

        var author = testDataTx.saveAuthor(testDataGenerator.generateAuthor());

        var program = testDataTx.saveProgram(testDataGenerator.generateProgram(author.getId()));

        var task = testDataTx.saveTask(testDataGenerator.generateTask(program.getId(), 1));

        for(int i =0; i<10; i++){
            testDataTx.saveActionItem(testDataGenerator.generateAI(ActionItemType.TEXT, i, "", "", task.getId()));
        }

        this.mockMvc.perform(get(CONTROLLER_URL_ROOT_PREFIX + "/task/{taskId}/items",  task.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.numberOfElements").value("10"));
    }



    @Test
    void updateBasicProgramData() throws Exception {

        var author = testDataTx.saveAuthor(testDataGenerator.generateAuthor());

        var program = testDataTx.saveProgram(testDataGenerator.generateProgram(author.getId()));
        var updatedDesc = UUID.randomUUID().toString();


        this.mockMvc.perform(patch(CONTROLLER_URL_ROOT_PREFIX + "/author/{authorId}/programs/{programId}", author.getId(), program.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(serialize(Map.of("description", updatedDesc))))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value(updatedDesc));
    }


    @Test
    void changeProgramStatus() throws Exception {
        var author = testDataTx.saveAuthor(testDataGenerator.generateAuthor());

        var program = testDataTx.saveProgram(testDataGenerator.generateProgram(author.getId()));

        this.mockMvc.perform(post(CONTROLLER_URL_ROOT_PREFIX + "/author/{authorId}/programs/{programId}/publish", author.getId(), program.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.visibilityStatus").value(PUBLISHED.toString()));

        this.mockMvc.perform(post(CONTROLLER_URL_ROOT_PREFIX + "/author/{authorId}/programs/{programId}/archive", author.getId(), program.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.visibilityStatus").value(ARCHIVED.toString()));

        this.mockMvc.perform(post(CONTROLLER_URL_ROOT_PREFIX + "/author/{authorId}/programs/{programId}/publish", UUID.randomUUID(), program.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        this.mockMvc.perform(post(CONTROLLER_URL_ROOT_PREFIX + "/author/{authorId}/programs/{programId}/archive", UUID.randomUUID(), program.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateBasicProgramDataNonExistingField() throws Exception {
        var author = testDataTx.saveAuthor(testDataGenerator.generateAuthor());

        var program = testDataTx.saveProgram(testDataGenerator.generateProgram(author.getId()));
        var updatedDesc = UUID.randomUUID().toString();


        this.mockMvc.perform(patch(CONTROLLER_URL_ROOT_PREFIX + "/author/{authorId}/programs/{programId}", author.getId(), program.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(serialize(Map.of("description2", updatedDesc))))
                .andExpect(status().isBadRequest());
    }


    @Test
    void deleteProgramByAuthor() throws Exception {
        var author = testDataTx.saveAuthor(testDataGenerator.generateAuthor());

        var program = testDataTx.saveProgram(testDataGenerator.generateProgram(author.getId()));
        var task = testDataTx.saveTask(testDataGenerator.generateTask(program.getId(), 1));
        var task2 = testDataTx.saveTask(testDataGenerator.generateTask(program.getId(), 2));

        for(int i =0; i<10; i++){
            testDataTx.saveActionItem(testDataGenerator.generateAI(ActionItemType.TEXT, i, "", "", task.getId()));
            testDataTx.saveActionItem(testDataGenerator.generateAI(ActionItemType.VIDEO, i, "", "", task2.getId()));
        }


        this.mockMvc.perform(delete(CONTROLLER_URL_ROOT_PREFIX + "/author/{authorId}/programs/{programId}", author.getId(), program.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }


    @Test
    void deleteProgramFraud() throws Exception {
        var author = testDataTx.saveAuthor(testDataGenerator.generateAuthor());

        var program = testDataTx.saveProgram(testDataGenerator.generateProgram(author.getId()));
        this.mockMvc.perform(delete(CONTROLLER_URL_ROOT_PREFIX + "/author/{authorId}/programs/{programId}", UUID.randomUUID(), program.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }


    protected String serialize(Object entity) throws JsonProcessingException {
        ObjectMapper o = new ObjectMapper();
        return o.writeValueAsString(entity);
    }
}
