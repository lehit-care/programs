package com.lehit.programs.controller;

import com.lehit.common.enums.ExecutionStatus;
import com.lehit.programs.data.TestDataGenerator;
import com.lehit.programs.data.TestDataTx;
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
    private TestDataGenerator testDataGenerator;
    @Autowired
    private TasksService tasksService;


    @Autowired
    private ExecutionProgressService progressService;

    @Autowired
    protected MockMvc mockMvc;



    protected static final String CONTROLLER_URL_ROOT_PREFIX = "/api/v1/";

    @Test
    void OpenStartedProgram() throws Exception{
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

        this.mockMvc.perform(get(CONTROLLER_URL_ROOT_PREFIX + "/client/{clientId}/programs/{programId}", clientId, program.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.lifecycleStatus").value(ExecutionStatus.STARTED.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.finishedAt").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.startedAt").isNotEmpty());
    }

    @Test
    void OpenNotStartedProgram() throws Exception{
        UUID clientId = UUID.randomUUID();

        var program = testDataTx.saveProgram(testDataGenerator.generateProgram());

        var task1 = testDataTx.saveTask(testDataGenerator.generateTask(program.getId(), 1));
        testDataTx.saveTask(testDataGenerator.generateTask(program.getId(), 2));



        this.mockMvc.perform(get(CONTROLLER_URL_ROOT_PREFIX + "/client/{clientId}/programs/{programId}", clientId, program.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.lifecycleStatus").value(ExecutionStatus.NEW.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.finishedAt").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.startedAt").isEmpty());

    }

}
