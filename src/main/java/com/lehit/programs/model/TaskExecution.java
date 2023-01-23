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

//todo maybe possible to get proxies?
@NamedEntityGraph(
        name = "include-cards",
        attributeNodes = {
                @NamedAttributeNode("executedCardUserRelations")
        }
)

@Entity
@Getter @Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor @AllArgsConstructor
@Builder @ToString(onlyExplicitlyIncluded = true)
public class TaskExecution {
    @Schema(hidden = true)
    @Id
    @Column(updatable= false)
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @EqualsAndHashCode.Include
    @ToString.Include
    private UUID id;

    private UUID userId;


    private int taskPosition;

    @DateTimeFormat(iso= DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime startedAt;

    @DateTimeFormat(iso= DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime finishedAt;

    private ExecutionStatus lifecycleStatus;

    @OneToMany(mappedBy = "taskExecution", fetch = FetchType.LAZY, orphanRemoval = true)
//    todo @RestResource(exported = false)
    @JsonIgnore
    private List<ExecutedItem> executedItemUserRelations;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "program_id", insertable = false, updatable = false)
//    todo @RestResource(exported = false)
    @JsonIgnore
    private Program program;

    @Column(name = "program_id")
    @EqualsAndHashCode.Include
    @ToString.Include
    private UUID programId;
}
