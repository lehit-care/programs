package com.lehit.programs.service;

import com.lehit.programs.data.TestDataGenerator;
import com.lehit.programs.data.TestDataTx;
import com.lehit.programs.model.enums.ActionItemType;
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
    private TestDataGenerator testDataGenerator;
    @Autowired
    private TasksService tasksService;


    @Autowired
    private TasksService taskService;



    @Test
    void deleteTaskByAuthor() {
        UUID authorId = UUID.randomUUID();

        var program = testDataTx.saveProgram(testDataGenerator.generateProgram(authorId));

        var task = testDataTx.saveTask(testDataGenerator.generateTask(program.getId(), 1));

        testDataTx.saveActionItem(testDataGenerator.generateAI(ActionItemType.TEXT, 1, "", "", task.getId()));

        taskService.deleteTask(authorId, task.getId());
    }


    @Test
    void deleteTaskByFraud() {
        UUID authorId = UUID.randomUUID();

        var program = testDataTx.saveProgram(testDataGenerator.generateProgram(authorId));

        var task = testDataTx.saveTask(testDataGenerator.generateTask(program.getId(), 1));

        assertThrows(IllegalStateException.class, () ->  taskService.deleteTask(UUID.randomUUID(), task.getId()));


    }

}
