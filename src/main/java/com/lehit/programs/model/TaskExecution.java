package com.lehit.programs.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lehit.common.enums.ExecutionStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@NamedEntityGraph(
        name = "including-tasks-and-item-executions",
        attributeNodes = {
                @NamedAttributeNode(value = "itemExecutions", subgraph = "executions-subgraph"),
                @NamedAttributeNode(value = "task")
        },
        subgraphs = {
                @NamedSubgraph(
                        name = "executions-subgraph",
                        attributeNodes = {
                                @NamedAttributeNode("actionItem")
                        }
                )
        }
)


@Entity
@Getter @Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor @AllArgsConstructor
@Builder @ToString
public class TaskExecution {
//    todo userId-TaskId - unique


    @Schema(hidden = true)
    @Id
    @Column(updatable= false)
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @EqualsAndHashCode.Include
    private UUID id;

    private UUID userId;


    @DateTimeFormat(iso= DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime startedAt;

    @DateTimeFormat(iso= DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime finishedAt;

    private ExecutionStatus lifecycleStatus;

    @ToString.Exclude
    @OneToMany(mappedBy = "taskExecution", fetch = FetchType.LAZY, orphanRemoval = true)
    @JsonIgnore
    private List<ItemExecution> itemExecutions;

    @ToString.Exclude
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "program_execution", insertable = false, updatable = false)
    @JsonIgnore
    private ProgramExecution programExecution;

    @Column(name = "program_execution")
    @EqualsAndHashCode.Include
    private UUID programExecutionId;


    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", insertable = false, updatable = false)
    private Task task;

    @Column(name = "task_id")
    private UUID taskId;
}
