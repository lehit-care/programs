package com.lehit.programs.controller;

import com.lehit.common.enums.ExecutionStatus;
import com.lehit.programs.data.TestDataGenerator;
import com.lehit.programs.data.TestDataTx;
import com.lehit.programs.model.enums.ActionItemType;
import com.lehit.programs.model.payload.ExecutedItemRequest;
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

import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Slf4j
class ExecutionControllerTest {
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
    void getActiveProgramStructure() throws Exception{
        UUID clientId = UUID.randomUUID();

        var program = testDataTx.saveProgram(testDataGenerator.generateProgram());

        var task1 = testDataTx.saveTask(testDataGenerator.generateTask(program.getId(), 1));
        testDataTx.saveTask(testDataGenerator.generateTask(program.getId(), 2));


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

        this.mockMvc.perform(get(CONTROLLER_URL_ROOT_PREFIX + "/executions/{clientId}/current-program", clientId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.lifecycleStatus").value(ExecutionStatus.STARTED.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.finishedAt").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.startedAt").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.taskExecutions", hasSize(2)));
    }

    @Test
    void startTheSameProgramTwice() throws Exception{
        UUID clientId = UUID.randomUUID();

        var program = testDataTx.saveProgram(testDataGenerator.generateProgram());

        this.mockMvc.perform(post(CONTROLLER_URL_ROOT_PREFIX + "/executions/{clientId}/start-program/{programId}", clientId, program.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.lifecycleStatus").value(ExecutionStatus.STARTED.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.finishedAt").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.startedAt").isNotEmpty());

        this.mockMvc.perform(post(CONTROLLER_URL_ROOT_PREFIX + "/executions/{clientId}/start-program/{programId}", clientId, program.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }


    @Test
    void startTheSameTaskTwice() throws Exception{
        UUID clientId = UUID.randomUUID();

        var program = testDataTx.saveProgram(testDataGenerator.generateProgram());

        var task1 = testDataTx.saveTask(testDataGenerator.generateTask(program.getId(), 1));

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

// todo check
//        this.mockMvc.perform(post(CONTROLLER_URL_ROOT_PREFIX + "/executions/{clientId}/start-task/{taskId}", clientId, task1.getId())
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest());
    }


    @Test
    void getActiveProgramStructureNotStarted() throws Exception{
        UUID clientId = UUID.randomUUID();

        this.mockMvc.perform(get(CONTROLLER_URL_ROOT_PREFIX + "/executions/{clientId}/current-program", clientId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }


    @Test
    void getActiveExecutionStructure() throws Exception {
        UUID clientId = UUID.randomUUID();

        var program = testDataTx.saveProgram(testDataGenerator.generateProgram());

        var task1 = testDataTx.saveTask(testDataGenerator.generateTask(program.getId(), 1));
        var ai1 = testDataTx.saveActionItem(testDataGenerator.generateAI(ActionItemType.TEXT, 1, "", "", task1.getId()));
        testDataTx.saveActionItem(testDataGenerator.generateAI(ActionItemType.PICTURE, 2, "", "", task1.getId()));

        progressService.assignProgram(clientId, program.getId());

        var taskExe = progressService.startTaskExecution(clientId, task1.getId());

        progressService.executeItem(new ExecutedItemRequest(taskExe.getId(), "", null, ActionItemType.FREE_TEXT_QUESTION),clientId, ai1.getId());


        this.mockMvc.perform(get(CONTROLLER_URL_ROOT_PREFIX + "/executions/{clientId}/task-exe/{taskExecutionId}", clientId, taskExe.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.itemExecutions[0].lifecycleStatus").value(ExecutionStatus.FINISHED.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.itemExecutions[1].lifecycleStatus").value(ExecutionStatus.NEW.toString()));
    }



    @Test
    void getAllPrograms() throws Exception {

        for(int i=0; i<30; i++)
            testDataTx.saveProgram(testDataGenerator.generateProgram());



        this.mockMvc.perform(get(CONTROLLER_URL_ROOT_PREFIX + "/programs")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements").value("30"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.pageable.pageSize").value("20"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.numberOfElements").value("20"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.last").value(false));
    }



    @Test
    void getUserActionedProgramsList() throws Exception{
        UUID clientId = UUID.randomUUID();

        var program = testDataTx.saveProgram(testDataGenerator.generateProgram());
        var program2 = testDataTx.saveProgram(testDataGenerator.generateProgram());
        var program3 = testDataTx.saveProgram(testDataGenerator.generateProgram());


        this.mockMvc.perform(post(CONTROLLER_URL_ROOT_PREFIX + "/executions/{clientId}/start-program/{programId}", clientId, program.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.lifecycleStatus").value(ExecutionStatus.STARTED.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.finishedAt").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.startedAt").isNotEmpty());

        this.mockMvc.perform(post(CONTROLLER_URL_ROOT_PREFIX + "/executions/{clientId}/start-program/{programId}", clientId, program2.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.lifecycleStatus").value(ExecutionStatus.STARTED.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.finishedAt").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.startedAt").isNotEmpty());

        this.mockMvc.perform(get(CONTROLLER_URL_ROOT_PREFIX + "/executions/{clientId}/programs-exe", clientId)
                        .queryParam("lcs", ExecutionStatus.STARTED.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.numberOfElements").value("2"));


        var exe3 = progressService.assignProgram(clientId, program3.getId());

        this.mockMvc.perform(post(CONTROLLER_URL_ROOT_PREFIX + "/executions/{clientId}/finish-program/{programExecutionId}", clientId, exe3.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.lifecycleStatus").value(ExecutionStatus.FINISHED.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.finishedAt").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.startedAt").isNotEmpty());

        this.mockMvc.perform(get(CONTROLLER_URL_ROOT_PREFIX + "/executions/{clientId}/programs-exe", clientId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.numberOfElements").value("3"));
    }
}
