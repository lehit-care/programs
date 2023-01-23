package com.lehit.programs.model.payload;

import com.lehit.programs.model.Task;


public record ProgramTasksProgress(Task task, long programTasksCount){
}
