package com.lehit.programs.service;

import com.lehit.programs.data.TestDataGenerator;
import com.lehit.programs.data.TestDataTx;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Slf4j
@ActiveProfiles("test")
public class TaskServiceTest {

    @Autowired
    private TestDataTx testDataTx;
    @Autowired
    private TasksService tasksService;


    @Autowired
    private TasksService taskService;



    @Test
    void deleteTaskByAuthor() {
        var author = testDataTx.saveAuthor(TestDataGenerator.generateAuthor());

        var program = testDataTx.saveProgram(TestDataGenerator.generateProgram(author.getId()));

        var task = testDataTx.saveTask(TestDataGenerator.generateTask(program.getId(), 1));

        testDataTx.saveActionItem(TestDataGenerator.generateAI(task.getId(), 1));

        taskService.deleteTask(author.getId(), task.getId());
    }


    @Test
    void deleteTaskByFraud() {
        var author = testDataTx.saveAuthor(TestDataGenerator.generateAuthor());

        var program = testDataTx.saveProgram(TestDataGenerator.generateProgram(author.getId()));

        var task = testDataTx.saveTask(TestDataGenerator.generateTask(program.getId(), 1));

        assertThrows(IllegalStateException.class, () ->  taskService.deleteTask(UUID.randomUUID(), task.getId()));


    }

}
