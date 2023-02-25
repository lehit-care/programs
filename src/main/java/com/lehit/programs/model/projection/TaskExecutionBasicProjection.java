package com.lehit.programs.model.projection;


import com.lehit.common.enums.ExecutionStatus;
import com.lehit.programs.model.Task;

import java.time.LocalDateTime;
import java.util.UUID;


public interface TaskExecutionBasicProjection {
      UUID getId();

      UUID getUserId();

      LocalDateTime getStartedAt();

      LocalDateTime getFinishedAt();

      ExecutionStatus getLifecycleStatus();

      Task getTask();
}
