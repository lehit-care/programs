package com.lehit.programs.model.projection;


import com.lehit.common.enums.ExecutionStatus;
import com.lehit.programs.model.Program;

import java.time.LocalDateTime;
import java.util.UUID;


public interface ProgramExecutionWithTaskExecutions {
      UUID getId();

      UUID getUserId();

      LocalDateTime getStartedAt();

      LocalDateTime getFinishedAt();

      ExecutionStatus getLifecycleStatus();

      Program getProgram();

      TaskExecutionBasicProjection getTaskExecutions();

}
