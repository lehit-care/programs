package com.lehit.programs.db.queries;


import com.lehit.programs.data.TestDataGenerator;
import com.lehit.programs.data.TestDataTx;
import com.lehit.programs.service.ExecutionProgressService;
import com.yannbriancon.interceptor.HibernateQueryInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
@Slf4j
@ActiveProfiles("test")
class ExecutionsDbTest{

    @Autowired
    private HibernateQueryInterceptor hibernateQueryInterceptor;
    @Autowired
    private TestDataTx testDataTx;
    @Autowired
    private TestDataGenerator testDataGenerator;
    @Autowired
    private ExecutionProgressService progressService;



    @Test
    void getActiveExecutionStructure() {
        UUID clientId = UUID.randomUUID();

        var author = testDataTx.saveAuthor(testDataGenerator.generateAuthor());

        var program = testDataTx.saveProgram(testDataGenerator.generateProgram(author.getId()));

        var task1 = testDataTx.saveTask(testDataGenerator.generateTask(program.getId(), 1));
        var task2 =  testDataTx.saveTask(testDataGenerator.generateTask(program.getId(), 2));
        var task3 =  testDataTx.saveTask(testDataGenerator.generateTask(program.getId(), 3));

        var data = progressService.assignProgram(clientId, program.getId());

        progressService.startTaskExecution(clientId, task1.getId());

        hibernateQueryInterceptor.startQueryCount();
        log.debug("start queryCount");

        var exe = progressService.getActiveProgramExecutionData(clientId);

        log.debug("end queryCount");
        long queriesCount = hibernateQueryInterceptor.getQueryCount();
//get step, start
        assertEquals(1, queriesCount);

//        log.debug("dfvfdv {}", exe);
    }



}
