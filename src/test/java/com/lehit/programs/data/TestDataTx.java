package com.lehit.programs.data;

import com.lehit.programs.model.*;
import com.lehit.programs.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Slf4j
@Transactional
public class TestDataTx {
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private ActionItemRepository actionItemRepository;
    @Autowired
    private TaskExecutionRepository taskExecutionRepository;
    @Autowired
    private ProgramRepository programRepository;
    @Autowired
    private AuthorRepository authorRepository;


    public Task saveTask(Task task){
        return taskRepository.save(task);
    }

    public ActionItem saveActionItem(ActionItem actionItem){
        return actionItemRepository.save(actionItem);
    }


    public TaskExecution setExecutionFinishedAt(UUID executionId, LocalDateTime time){
        TaskExecution execution = taskExecutionRepository.findById(executionId).orElseThrow();
        execution.setFinishedAt(time);
        return execution;
    }


    public Program saveProgram(Program program){
        return programRepository.save(program);
    }

    public Author saveAuthor(Author author){
        return authorRepository.save(author);
    }

}
