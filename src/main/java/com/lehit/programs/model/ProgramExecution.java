package com.lehit.programs.model;

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
        name = "including-programs-tasks-executions",
        attributeNodes = {
                @NamedAttributeNode(value = "taskExecutions", subgraph = "executions-subgraph"),
                @NamedAttributeNode(value = "program")
        },
        subgraphs = {
                @NamedSubgraph(
                        name = "executions-subgraph",
                        attributeNodes = {
                                @NamedAttributeNode("task")
                        }
                )
        }
)


@Entity
@Getter @Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor @AllArgsConstructor
@Builder @ToString
public class ProgramExecution {
//    todo only 1 in-progress
    @Schema(hidden = true)
    @Id
    @Column(updatable= false)
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @EqualsAndHashCode.Include
    @ToString.Include
    private UUID id;

    private UUID userId;


    @DateTimeFormat(iso= DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime startedAt;

    @DateTimeFormat(iso= DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime finishedAt;

    private ExecutionStatus lifecycleStatus;

    @OneToMany(mappedBy = "programExecution", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<TaskExecution> taskExecutions;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "program_id", insertable = false, updatable = false)
    private Program program;

    @Column(name = "program_id")
    @EqualsAndHashCode.Include
    @ToString.Include
    private UUID programId;

}
