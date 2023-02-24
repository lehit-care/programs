package com.lehit.programs.service;

import com.lehit.programs.data.TestDataGenerator;
import com.lehit.programs.data.TestDataTx;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

@SpringBootTest
@Slf4j
@ActiveProfiles("test")
public class ExecutionProgressServiceTest {

    @Autowired
    private TestDataTx testDataTx;
    @Autowired
    private TestDataGenerator testDataGenerator;
    @Autowired
    private TasksService tasksService;


    @Autowired
    private ExecutionProgressService progressService;


    @Test
    void getActiveExecutionStructure() {
        UUID clientId = UUID.randomUUID();

        var author = testDataTx.saveAuthor(testDataGenerator.generateAuthor());

        var program = testDataTx.saveProgram(testDataGenerator.generateProgram(author.getId()));

        var task1 = testDataTx.saveTask(testDataGenerator.generateTask(program.getId(), 1));
        var task2 =  testDataTx.saveTask(testDataGenerator.generateTask(program.getId(), 2));

        var data = progressService.assignProgram(clientId, program.getId());

        progressService.startTaskExecution(clientId, task1.getId());

        var exe = progressService.getActiveProgramExecutionData1(clientId);


        log.debug("dfvfdv {}", exe);


    }



}
