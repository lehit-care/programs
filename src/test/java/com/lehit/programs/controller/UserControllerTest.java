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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Slf4j
class UserControllerTest  {
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
                .andExpect(MockMvcResultMatchers.jsonPath("$.startedAt").isNotEmpty());

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
                .andExpect(MockMvcResultMatchers.jsonPath("$.itemExecutions[1].lifecycleStatus").isEmpty());
    }




}
