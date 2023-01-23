package com.lehit.programs.model.payload;

import com.lehit.common.enums.ExecutionStatus;
import com.lehit.programs.model.Task;


public record ProgramExecutionItem(Task task, ExecutionStatus executionStatus){
}
