package com.lehit.programs.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lehit.common.enums.ExecutionStatus;
import com.lehit.programs.model.audit.Auditable;
import com.lehit.programs.model.enums.ActionItemType;
import com.lehit.programs.model.pk.ExecutedItemCompositeKey;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.domain.Persistable;

import java.util.UUID;


@Entity
@Getter @Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@NoArgsConstructor @AllArgsConstructor
@Builder @ToString(onlyExplicitlyIncluded = true)
@IdClass(ExecutedItemCompositeKey.class)
public class ItemExecution extends Auditable implements Persistable<ExecutedItemCompositeKey> {
    @ToString.Include
    private UUID userId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", insertable = false, updatable = false)
    private ActionItem actionItem;

    @Column(name = "item_id")
    @EqualsAndHashCode.Include
    @Id
    @ToString.Include
    private UUID itemId;

    @ToString.Exclude
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "execution_task_id", insertable = false, updatable = false)
    @JsonIgnore
    private TaskExecution taskExecution;

    @Column(name = "execution_task_id")
    @EqualsAndHashCode.Include
    @Id
    @ToString.Include
    private UUID taskExecutionId;

    private ActionItemType itemType;

    private ExecutionStatus lifecycleStatus;


    //    avoid select before insert operation
    @Schema(hidden = true)
    @Override
    public ExecutedItemCompositeKey getId() {
        return ExecutedItemCompositeKey.builder()
                .itemId(this.itemId)
                .taskExecutionId(this.taskExecutionId)
                .build();
    }

    @JsonIgnore
    @Transient
    private boolean isNew = true;

    @JsonIgnore
    @Override
    public boolean isNew() {
        return isNew;
    }

    @PrePersist
    @PostLoad
    void markNotNew() {
        this.isNew = false;
    }


    public ItemExecution(UUID userId, UUID itemId, UUID taskExecutionId, ActionItemType actionItemType) {
        this.userId = userId;
        this.itemId = itemId;
        this.taskExecutionId = taskExecutionId;
        this.itemType = actionItemType;
    }

}

