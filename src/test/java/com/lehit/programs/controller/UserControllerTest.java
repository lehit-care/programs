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
    void startTask() throws Exception{
        UUID clientId = UUID.randomUUID();

        var program = testDataTx.saveProgram(testDataGenerator.generateProgram());

        var task1 = testDataTx.saveTask(testDataGenerator.generateTask(program.getId(), 1));
        var task2 =  testDataTx.saveTask(testDataGenerator.generateTask(program.getId(), 2));



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



//    @Test
//    @Order(1)
//    void finishTask() throws Exception{
//        UUID clientId = UUID.randomUUID();
//        int step = 18;
//        int position = 1;
//        var program = testDataTx.saveProgram(testDataGenerator.generateProgram());
//
//        userService.assignProgram(clientId, program.getId());
//        userService.saveUserStep(clientId, step);
//
//        Task task = testDataTx.saveTask(testDataGenerator.generateTask(program.getId(), step, position));
//        Task task2 = testDataTx.saveTask(testDataGenerator.generateTask(program.getId(), step, position+1));
//        Card card = testDataTx.saveCard(testDataGenerator.generateCard(CardType.FREE_TEXT_QUESTION, 1, null, null, task2.getId()));
//        TaskExecution taskExecution = executionProgressService.startTaskExecution(clientId,  position);
//
//        this.mockMvc.perform(post(CONTROLLER_URL_ROOT_PREFIX + "/commands/client/"+ clientId + "/finish-task/" +taskExecution.getId())
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.task.id").value(task2.getId().toString()))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.task.cards[0].id").value(card.getId().toString()));
//
//        Assertions.assertTrue(executionProgressService.getActiveExecution(clientId).isEmpty());
//    }
//
//    @Test
//    void startTheSameTask() throws Exception{
//        UUID clientId = UUID.randomUUID();
//        var program = testDataTx.saveProgram(testDataGenerator.generateProgram());
//
//        int step = testDataGenerator.generateRandomStep();
//        Task task = testDataTx.saveTask(testDataGenerator.generateTask(program.getId(), step, 1));
//        userService.saveUserStep(clientId, step);
//
//        this.mockMvc.perform(post(CONTROLLER_URL_ROOT_PREFIX + "/commands/client/{clientId}/start-task/{pos}", clientId, task.getPosition())
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.lifecycleStatus").value(ExecutionStatus.STARTED.toString()))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.finishedAt").isEmpty())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.startedAt").isNotEmpty());
//
//
//        this.mockMvc.perform(post(CONTROLLER_URL_ROOT_PREFIX + "/commands/client/{clientId}/start-task/{pos}", clientId, task.getPosition())
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isForbidden());
//    }
//
//
//    @Test
//    void executeCard() throws Exception{
//        UUID clientId = UUID.randomUUID();
//        var program = testDataTx.saveProgram(testDataGenerator.generateProgram());
//        int step = testDataGenerator.generateRandomStep();
//        Task task = testDataTx.saveTask(testDataGenerator.generateTask(program.getId(), step,1));
//        Card card = testDataTx.saveCard(testDataGenerator.generateCard(CardType.FREE_TEXT_QUESTION, 1, null, null, task.getId()));
//
//        userService.saveUserStep(clientId, step);
//        TaskExecution taskUserRelation = executionProgressService.startTaskExecution(clientId, task.getPosition());
//
//        ExecutedCard cardUserRelation = ExecutedCard.builder()
//                .taskExecutionId(taskUserRelation.getId())
//                .duration(4)
//                .reply("lol")
//                .build();
//
//        this.mockMvc.perform(post(CONTROLLER_URL_ROOT_PREFIX + "/commands/client/"+ clientId + "/execute-card/" +card.getId())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(serialize(cardUserRelation)))
//                .andExpect(status().isNoContent());
//    }
//
//    @Test
//    void executeOneCardFromTask() throws Exception{
//        UUID clientId = UUID.randomUUID();
//        var program = testDataTx.saveProgram(testDataGenerator.generateProgram());
//        int step = testDataGenerator.generateRandomStep();
//        Task task = testDataTx.saveTask(testDataGenerator.generateTask(program.getId(), step,1));
//
//        Card card = testDataTx.saveCard(testDataGenerator.generateCard(CardType.FREE_TEXT_QUESTION, 1, null, null, task.getId()));
//        Card card2 = testDataTx.saveCard(testDataGenerator.generateCard(CardType.VIDEO, 2, null, null,  task.getId()));
//        Card card3 = testDataTx.saveCard(testDataGenerator.generateCard(CardType.VIDEO, 3, null, null,  task.getId()));
//
//        userService.assignProgram(clientId, program.getId());
//        userService.saveUserStep(clientId, step);
//
//        TaskExecution taskUserRelation = executionProgressService.startTaskExecution(clientId, task.getPosition());
//
//        ExecutedCard cardUserRelation = ExecutedCard.builder()
//                .taskExecutionId(taskUserRelation.getId())
//                .duration(4)
//                .reply("lol")
//                .build();
//
//        this.mockMvc.perform(post(CONTROLLER_URL_ROOT_PREFIX + "/commands/client/"+ clientId + "/execute-card/" +card.getId())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(serialize(cardUserRelation)))
//                .andExpect(status().isNoContent());
//
//
//        this.mockMvc.perform(get(CONTROLLER_URL_ROOT_PREFIX + "/commands/client/"+ clientId + "/continue-execution")
//                        .queryParam("step", Integer.toString(step))
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.executedCardIds[0]").value(String.valueOf(card.getId())))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.task.cards[0].id").value(card.getId().toString()))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.task.cards[1].id").value(card2.getId().toString()))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.task.cards[2].id").value(card3.getId().toString()))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.task.id").value(task.getId().toString()));
//    }
//
//    @Test
//    void continueExecutionFinishedTask() throws Exception{
//        UUID clientId = UUID.randomUUID();
//        var program = testDataTx.saveProgram(testDataGenerator.generateProgram());
//        int step = testDataGenerator.generateRandomStep();
//        Task task = testDataTx.saveTask(testDataGenerator.generateTask(program.getId(), step,1));
//        Card card = testDataTx.saveCard(testDataGenerator.generateCard(CardType.FREE_TEXT_QUESTION, 1, null, null, task.getId()));
//
//        Task task2 = testDataTx.saveTask(testDataGenerator.generateTask(program.getId(), step,2));
//        Card card2 = testDataTx.saveCard(testDataGenerator.generateCard(CardType.FREE_TEXT_QUESTION, 1, null, null, task2.getId()));
//
//
//        userService.assignProgram(clientId, program.getId());
//        userService.saveUserStep(clientId, step);
//        TaskExecution taskUserRelation = executionProgressService.startTaskExecution(clientId, task.getPosition());
//
//        ExecutedCard cardUserRelation = ExecutedCard.builder()
//                .taskExecutionId(taskUserRelation.getId())
//                .duration(4)
//                .reply("lol")
//                .build();
//
//        this.mockMvc.perform(post(CONTROLLER_URL_ROOT_PREFIX + "/commands/client/"+ clientId + "/execute-card/" +card.getId())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(serialize(cardUserRelation)))
//                .andExpect(status().isNoContent());
//
//        this.mockMvc.perform(post(CONTROLLER_URL_ROOT_PREFIX + "/commands/client/{clientId}/finish-task/{taskExecutionId}", clientId, taskUserRelation.getId())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(serialize(cardUserRelation)))
//                .andExpect(status().isOk());
//
//
//
//        this.mockMvc.perform(get(CONTROLLER_URL_ROOT_PREFIX + "/commands/client/{clientId}/continue-execution", clientId)
//                        .queryParam("step", Integer.toString(step))
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.executedCardIds").isEmpty())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.stepTasksCount").value("2"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.task.cards[0].id").value(card2.getId().toString()))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.task.id").value(task2.getId().toString()));
//    }
//
//    @Test
//    void continueExecutionInitialStep() throws Exception{
//        UUID clientId = UUID.randomUUID();
//        var program = testDataTx.saveProgram(testDataGenerator.generateProgram());
//        int step = testDataGenerator.generateRandomStep();
//
//        userService.assignProgram(clientId, program.getId());
//        userService.saveUserStep(clientId, step);
//
//        Task task = testDataTx.saveTask(testDataGenerator.generateTask(program.getId(), step,1));
//        Card card = testDataTx.saveCard(testDataGenerator.generateCard(CardType.FREE_TEXT_QUESTION, 1, null, null, task.getId()));
//
//        Task task2 = testDataTx.saveTask(testDataGenerator.generateTask(program.getId(), step,2));
//        Card card2 = testDataTx.saveCard(testDataGenerator.generateCard(CardType.FREE_TEXT_QUESTION, 1, null, null, task2.getId()));
//
//
//        this.mockMvc.perform(get(CONTROLLER_URL_ROOT_PREFIX + "/commands/client/{clientId}/continue-execution", clientId)
//                        .queryParam("step", Integer.toString(step))
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.executedCardIds").isEmpty())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.stepTasksCount").value("2"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.task.cards[0].id").value(card.getId().toString()))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.task.id").value(task.getId().toString()));
//    }
//
//    @Test
//    void continueExecutionNextStep() throws Exception{
//        UUID clientId = UUID.randomUUID();
//        var program = testDataTx.saveProgram(testDataGenerator.generateProgram());
//        int step1 = testDataGenerator.generateRandomStep();
//        int step2  = step1+1;
//
//        Task task11 = testDataTx.saveTask(testDataGenerator.generateTask(program.getId(),step1,1));
//        Task task12 = testDataTx.saveTask(testDataGenerator.generateTask(program.getId(),step1,2));
//
//        Task task21 = testDataTx.saveTask(testDataGenerator.generateTask(program.getId(),step2,1));
//        Card card = testDataTx.saveCard(testDataGenerator.generateCard(CardType.FREE_TEXT_QUESTION, 1, null, null, task21.getId()));
//
//        Task task22 = testDataTx.saveTask(testDataGenerator.generateTask(program.getId(),step2,2));
//
//        userService.assignProgram(clientId, program.getId());
//        userService.saveUserStep(clientId, step1);
//
//        var exe11 = executionProgressService.startTaskExecution(clientId, 1);
//        executionProgressService.finishTaskExecution(clientId, exe11.getId());
//        var exe12 = executionProgressService.startTaskExecution(clientId, 2);
//        executionProgressService.finishTaskExecution(clientId, exe12.getId());
//
//        userService.saveUserStep(clientId, step2);
//
//
//        this.mockMvc.perform(get(CONTROLLER_URL_ROOT_PREFIX + "/commands/client/{clientId}/continue-execution", clientId)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.executedCardIds").isEmpty())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.stepTasksCount").value("2"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.task.cards[0].id").value(card.getId().toString()))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.task.id").value(task21.getId().toString()));
//    }
//
//
//
//    @Test
//    void checkPreviousStepReplies() throws Exception{
//        UUID clientId = UUID.randomUUID();
//        var program = testDataTx.saveProgram(testDataGenerator.generateProgram());
//        int step = testDataGenerator.generateRandomStep();
//        Task task = testDataTx.saveTask(testDataGenerator.generateTask(program.getId(), step,1));
//
//        Card card = testDataTx.saveCard(testDataGenerator.generateCard(CardType.FREE_TEXT_QUESTION, 1, null, null, task.getId()));
//        Card card2 = testDataTx.saveCard(testDataGenerator.generateCard(CardType.VIDEO, 2, null, null,  task.getId()));
//        Card card3 = testDataTx.saveCard(testDataGenerator.generateCard(CardType.VIDEO, 3, null, null,  task.getId()));
//
//        userService.saveUserStep(clientId, step);
//        TaskExecution taskUserRelation = executionProgressService.startTaskExecution(clientId, task.getPosition());
//
//        ExecutedCard cardUserRelation = ExecutedCard.builder()
//                .taskExecutionId(taskUserRelation.getId())
//                .duration(4)
//                .reply("lol")
//                .build();
//
//        this.mockMvc.perform(post(CONTROLLER_URL_ROOT_PREFIX + "/commands/client/"+ clientId + "/execute-card/" +card.getId())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(serialize(cardUserRelation)))
//                .andExpect(status().isNoContent());
//
//
//        ExecutedCard cardUserRelation2 = ExecutedCard.builder()
//                .taskExecutionId(taskUserRelation.getId())
//                .duration(4)
//                .reply("lol2")
//                .build();
//
//        this.mockMvc.perform(post(CONTROLLER_URL_ROOT_PREFIX + "/commands/client/"+ clientId + "/execute-card/" +card2.getId())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(serialize(cardUserRelation2)))
//                .andExpect(status().isNoContent());
//
//
//        this.mockMvc.perform(get(CONTROLLER_URL_ROOT_PREFIX + "/card-replies/search/by-step-task")
//                        .queryParam("userId", clientId.toString())
//                        .queryParam("step", String.valueOf(step))
//                        .queryParam("taskPosition", "1")
//                        .queryParam("projection", "including-card")
//                        .queryParam("sort", "createdAt,asc")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.card-replies[0].reply").value(cardUserRelation.getReply()))
//                .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.card-replies[0].card.id").value(card.getId().toString()))
//                .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.card-replies[1].reply").value(cardUserRelation2.getReply()));
//
//    }
//
//
//    @Test
//    void searchExecutionHistoryList() throws Exception{
//        int step = testDataGenerator.generateRandomStep();
//        var program = testDataTx.saveProgram(testDataGenerator.generateProgram());
//
//        Task task = testDataTx.saveTask(testDataGenerator.generateTask(program.getId(),step,1));
//        Task task2 = testDataTx.saveTask(testDataGenerator.generateTask(program.getId(),step,2));
//
//
//        Card card = testDataTx.saveCard(testDataGenerator.generateCard(CardType.FREE_TEXT_QUESTION, 1, null, null, task.getId()));
//        Card card2 = testDataTx.saveCard(testDataGenerator.generateCard(CardType.VIDEO, 2, null, null,  task.getId()));
//        Card card3 = testDataTx.saveCard(testDataGenerator.generateCard(CardType.VIDEO, 3, null, null,  task.getId()));
//
//
//
//        this.mockMvc.perform(get(CONTROLLER_URL_ROOT_PREFIX + "/title-list/tasks")
//                        .queryParam("step", Integer.toString(step))
//                        .queryParam("programId", program.getId().toString())
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].position").value("1"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].id").value(task.getId().toString()))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.[1].position").value("2"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.[1].id").value(task2.getId().toString()));
//
//
//        this.mockMvc.perform(get(CONTROLLER_URL_ROOT_PREFIX + "/title-list/cards")
//                        .queryParam("taskId", task.getId().toString())
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].position").value("1"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].id").value(card.getId().toString()))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.[1].position").value("2"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.[1].id").value(card2.getId().toString()));
//
//    }

}
