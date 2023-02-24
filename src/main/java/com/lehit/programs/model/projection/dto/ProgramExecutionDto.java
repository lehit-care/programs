package com.lehit.programs.model.projection.dto;

import com.lehit.common.enums.ExecutionStatus;
import com.lehit.programs.model.Program;
import com.lehit.programs.model.TaskExecution;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder @ToString
public class ProgramExecutionDto {

    private UUID id;

    private UUID userId;


    @DateTimeFormat(iso= DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime startedAt;

    @DateTimeFormat(iso= DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime finishedAt;

    private ExecutionStatus lifecycleStatus;

    private List<TaskExecution> taskExecutions;

    private Program program;
}
