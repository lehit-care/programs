package com.lehit.programs.model.projection;


import com.lehit.common.enums.ExecutionStatus;
import com.lehit.programs.model.ItemExecution;
import com.lehit.programs.model.Task;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


public interface TaskExecutionWithItemsProjection {
      UUID getId();

      UUID getUserId();

      LocalDateTime getStartedAt();

      LocalDateTime getFinishedAt();

      ExecutionStatus getLifecycleStatus();

      List<ItemExecution> getItemExecutions();

      Task getTask();
}
