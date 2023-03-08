package com.lehit.programs.controller;

import com.lehit.common.enums.ExecutionStatus;
import com.lehit.programs.data.TestDataGenerator;
import com.lehit.programs.data.TestDataTx;
import com.lehit.programs.model.enums.ContentVisibilityStatus;
import com.lehit.programs.service.ExecutionProgressService;
import com.lehit.programs.service.ProgramsService;
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

import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Slf4j
class ClientControllerTest {
    @Autowired
    private TestDataTx testDataTx;
    @Autowired
    private TasksService tasksService;
    @Autowired
    private ProgramsService programsService;


    @Autowired
    private ExecutionProgressService progressService;

    @Autowired
    protected MockMvc mockMvc;



    protected static final String CONTROLLER_URL_ROOT_PREFIX = "/api/v1/";

    @Test
    void OpenStartedProgram() throws Exception{
        UUID clientId = UUID.randomUUID();

        var author = testDataTx.saveAuthor(TestDataGenerator.generateAuthor());

        var program = testDataTx.saveProgram(TestDataGenerator.generateProgram(author.getId()));

        var task1 = testDataTx.saveTask(TestDataGenerator.generateTask(program.getId(), 1));
        testDataTx.saveTask(TestDataGenerator.generateTask(program.getId(), 2));



        this.mockMvc.perform(post(CONTROLLER_URL_ROOT_PREFIX + "/executions/{clientId}/start-program/{programId}", clientId, program.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.lifecycleStatus").value(ExecutionStatus.STARTED.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.finishedAt").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.startedAt").isNotEmpty());

        this.mockMvc.perform(post(CONTROLLER_URL_ROOT_PREFIX + "/executions/{clientId}/start-task/{taskId}", clientId, task1.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.lifecycleStatus").value(ExecutionStatus.STARTED.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.finishedAt").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.startedAt").isNotEmpty());

        this.mockMvc.perform(get(CONTROLLER_URL_ROOT_PREFIX + "/client/{clientId}/programs/{programId}", clientId, program.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.lifecycleStatus").value(ExecutionStatus.STARTED.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.finishedAt").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.startedAt").isNotEmpty());
    }


    @Test
    void OpenStartedProgramCheckTasksOrder() throws Exception{
        UUID clientId = UUID.randomUUID();

        var author = testDataTx.saveAuthor(TestDataGenerator.generateAuthor());

        var program = testDataTx.saveProgram(TestDataGenerator.generateProgram(author.getId()));

        var task1 = testDataTx.saveTask(TestDataGenerator.generateTask(program.getId(), 1));
        var task2 = testDataTx.saveTask(TestDataGenerator.generateTask(program.getId(), 2));



        this.mockMvc.perform(post(CONTROLLER_URL_ROOT_PREFIX + "/executions/{clientId}/start-program/{programId}", clientId, program.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.lifecycleStatus").value(ExecutionStatus.STARTED.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.finishedAt").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.startedAt").isNotEmpty());



        this.mockMvc.perform(get(CONTROLLER_URL_ROOT_PREFIX + "/client/{clientId}/programs/{programId}", clientId, program.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.taskExecutions[0].taskId").value(task1.getId().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.taskExecutions[1].taskId").value(task2.getId().toString()));
    }

    @Test
    void OpenNotStartedProgram() throws Exception{
        UUID clientId = UUID.randomUUID();

        var author = testDataTx.saveAuthor(TestDataGenerator.generateAuthor());

        var program = testDataTx.saveProgram(TestDataGenerator.generateProgram(author.getId()));

        var task1 = testDataTx.saveTask(TestDataGenerator.generateTask(program.getId(), 1));
        testDataTx.saveTask(TestDataGenerator.generateTask(program.getId(), 2));



        this.mockMvc.perform(get(CONTROLLER_URL_ROOT_PREFIX + "/client/{clientId}/programs/{programId}", clientId, program.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.lifecycleStatus").value(ExecutionStatus.NEW.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.finishedAt").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.startedAt").isEmpty());

    }


    @Test
    void searchPrograms() throws Exception{
        String titleBase = UUID.randomUUID().toString();
        var author = testDataTx.saveAuthor(TestDataGenerator.generateAuthor());
        var cat = testDataTx.saveCategory(TestDataGenerator.generateCategory());


        var program1 = testDataTx.saveProgram(TestDataGenerator.generateProgram(author.getId(), titleBase));
        var program2 = testDataTx.saveProgram(TestDataGenerator.generateProgram(author.getId(), titleBase+"srferf"));
        var program3 = testDataTx.saveProgram(TestDataGenerator.generateProgram(author.getId(),"dsrkjvcndk "+titleBase));
        var program4 = testDataTx.saveProgram(TestDataGenerator.generateProgram(author.getId(),"dsrkjvcndk "+titleBase+"srferf"));

        var programCat = testDataTx.saveProgram(TestDataGenerator.generateProgram(author.getId(), cat.getId()));


        var programNotMatchingTitle = testDataTx.saveProgram(TestDataGenerator.generateProgram(author.getId(), UUID.randomUUID().toString()));



        this.mockMvc.perform(get(CONTROLLER_URL_ROOT_PREFIX + "/client/programs/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .queryParam("title", titleBase))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.numberOfElements").value("0"));

        List.of(program1, program2, program3, program4)
                .forEach(program ->  programsService.changeProgramVisibilityStatus(author.getId(), program.getId(), ContentVisibilityStatus.PUBLISHED));


        this.mockMvc.perform(get(CONTROLLER_URL_ROOT_PREFIX + "/client/programs/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .queryParam("title", titleBase))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.numberOfElements").value("4"));


        this.mockMvc.perform(get(CONTROLLER_URL_ROOT_PREFIX + "/client/programs/search")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
//                .andExpect(MockMvcResultMatchers.jsonPath("$.content", hasSize(2)));
//                .andExpect(MockMvcResultMatchers.jsonPath("$.content", hasItem(programNotMatchingTitle.getId().toString())));
//                .andExpect(MockMvcResultMatchers.jsonPath("$.numberOfElements").ofType[Int].greaterThan(5).value("5"));

        this.mockMvc.perform(get(CONTROLLER_URL_ROOT_PREFIX + "/client/programs/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .queryParam("categoryId", cat.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.numberOfElements").value("0"));

        programsService.changeProgramVisibilityStatus(author.getId(), programCat.getId(), ContentVisibilityStatus.PUBLISHED);

        this.mockMvc.perform(get(CONTROLLER_URL_ROOT_PREFIX + "/client/programs/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .queryParam("categoryId", cat.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.numberOfElements").value("1"));



    }

}
