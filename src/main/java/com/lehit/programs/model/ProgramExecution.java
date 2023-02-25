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
        name = "base-exe-including-programs-tasks-executions",
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


@NamedEntityGraph(
        name = "base-exe-including-program",
        attributeNodes = {
                @NamedAttributeNode(value = "program")
        }
)

@NamedNativeQuery(
        name = "ProgramExecution.findProgramExecutionsWithTaskExecutionsWithTasks",
        hints = @QueryHint(name = "org.hibernate.readOnly", value = "true"),
        query =
                """
                               SELECT *
                                                       FROM (
                                                           SELECT
                                                               *,
                                                               DENSE_RANK() OVER (
                                                                   ORDER BY "p_e.started_at" DESC
                                                               ) rank from(
                                                                        select
                                                                           p_e.id as "p_e.id",
                                                                           p_e.finished_at as "p_e.finished_at",
                                                                           p_e.lifecycle_status as "p_e.lifecycle_status",
                                                                           p_e.user_id as "p_e.user_id",
                                                                           p_e.program_id as "p_e.program_id",
                                                                           p_e.started_at as "p_e.started_at",
                                                                           
                                                                           program.id as "program.id",
                                                                           program.author_id as "program.author_id",
                                                                           program.avatar_url as "program.avatar_url",
                                                                           program.description as "program.description",
                                                                           program.title as "program.title",
                                                                           program.visibility_status as "program.visibility_status",
                                                                           
                                                                           
                                                                           t_e.program_execution as "t_e.program_execution",
                                                                           t_e.id as "t_e.id",
                                                                           t_e.created_at as "t_e.created_at",
                                                                           t_e.finished_at as "t_e.finished_at",
                                                                           t_e.lifecycle_status as "t_e.lifecycle_status",
                                                                           t_e.started_at as "t_e.started_at",
                                                                           t_e.task_id as "t_e.task_id",
                                                                           t_e.user_id as "t_e.user_id",
                                                                           
                                                                           task.id as "task.id",
                                                                           task.title as "task.title",
                                                                           task.avatar_url as "task.avatar_url",
                                                                           task.description as "task.description",
                                                                           task.position as "task.position",
                                                                           task.program_id as "task.program_id"
                                                                         
                                                                           
                                                                       from
                                                                           program_execution p_e
                                                                       left join
                                                                           program program
                                                                               on program.id=p_e.program_id
                                                                       left join
                                                                           task_execution t_e
                                                                               on p_e.id=t_e.program_execution
                                                                       left join
                                                                           task task
                                                                               on task.id=t_e.task_id
                                                                       where
                                                                           p_e.user_id= :user
                                                                           and p_e.lifecycle_status=1
                                                                       order by
                                                                           p_e.started_at desc,
                                                                           t_e.created_at  ) pe_tc
                                                                    ) pe_tc_r WHERE pe_tc_r.rank <= :rank
                        """
        , resultSetMapping = "ProgramExecutionsWithTaskExecutionsWithTasks"
)
@SqlResultSetMapping(
        name = "ProgramExecutionsWithTaskExecutionsWithTasks",
        entities = {
                @EntityResult(
                        entityClass = ProgramExecution.class,
                        fields = {
                                @FieldResult(name = "id", column = "p_e.id"),
//                                @FieldResult(name = "createdAt", column = "p_e.created_at"),
                                @FieldResult(name = "finishedAt", column = "p_e.finished_at"),
                                @FieldResult(name = "startedAt", column = "p_e.started_at"),
                                @FieldResult(name = "lifecycleStatus", column = "p_e.lifecycle_status"),
                                @FieldResult(name = "program", column = "program.id"),
                                @FieldResult(name = "userId", column = "p_e.user_id")
                        }
                ),
                @EntityResult(
                        entityClass = Program.class,
                        fields = {
                                @FieldResult(name = "id", column = "program.id"),
                                @FieldResult(name = "title", column = "program.title"),
                                @FieldResult(name = "avatarUrl", column = "program.avatar_url"),
                                @FieldResult(name = "description", column = "program.description"),
                                @FieldResult(name = "author", column = "program.author_id"),
                                @FieldResult(name = "visibilityStatus", column = "program.visibility_status")
                        }
                ),
                @EntityResult(
                        entityClass = TaskExecution.class,
                        fields = {
                                @FieldResult(name = "id", column = "t_e.id"),
//                                @FieldResult(name = "createdAt", column = "t_e.created_at"),
                                @FieldResult(name = "finishedAt", column = "t_e.finished_at"),
                                @FieldResult(name = "startedAt", column = "t_e.started_at"),
                                @FieldResult(name = "lifecycleStatus", column = "t_e.lifecycle_status"),
                                @FieldResult(name = "userId", column = "t_e.user_id"),
                                @FieldResult(name = "programExecution", column = "t_e.program_execution"),
                                @FieldResult(name = "task", column = "t_e.task_id")
                        }
                ),
                @EntityResult(
                        entityClass = Task.class,
                        fields = {
                                @FieldResult(name = "id", column = "task.id"),
                                @FieldResult(name = "title", column = "task.title"),
                                @FieldResult(name = "avatarUrl", column = "task.avatar_url"),
                                @FieldResult(name = "description", column = "task.description"),
                                @FieldResult(name = "position", column = "task.position"),
                                @FieldResult(name = "program", column = "program.id")
                        }
                )
        }
)

@Entity
@Table(uniqueConstraints={
        @UniqueConstraint(columnNames = {"user_id", "program_id", "lifecycle_status"})
})
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

    @Column(name = "user_id")
    private UUID userId;


    @DateTimeFormat(iso= DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime startedAt;

    @DateTimeFormat(iso= DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime finishedAt;

    @Column(name = "lifecycle_status")
    private ExecutionStatus lifecycleStatus;

    @OneToMany(mappedBy = "programExecution", fetch = FetchType.LAZY, orphanRemoval = true)
    @OrderBy("createdAt")
    private List<TaskExecution> taskExecutions;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "program_id", insertable = false, updatable = false)
    private Program program;

    @Column(name = "program_id")
    @ToString.Include
    private UUID programId;
}
