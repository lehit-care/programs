package com.lehit.programs.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lehit.common.enums.Language;
import com.lehit.programs.model.audit.Auditable;
import com.lehit.programs.model.enums.ActionItemType;
import com.lehit.programs.model.pk.ExecutedItemCompositeKey;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.domain.Persistable;

import java.util.UUID;

@NamedEntityGraph(
        name = "include-card",
        attributeNodes = {
                @NamedAttributeNode("card")
        }
)

@Entity
@Getter @Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@NoArgsConstructor @AllArgsConstructor
@Builder @ToString(onlyExplicitlyIncluded = true)
@IdClass(ExecutedItemCompositeKey.class)
public class ExecutedItem extends Auditable implements Persistable<ExecutedItemCompositeKey> {

    @Schema(hidden = true)
    @ToString.Include
    private UUID userId;


    @Schema(hidden = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", insertable = false, updatable = false)
//   todo @RestResource(exported = false)
    @JsonIgnore
    private ActionItem actionItem;

    @Column(name = "item_id")
    @EqualsAndHashCode.Include
    @Id
    @ToString.Include
    private UUID itemId;

    @Schema(hidden = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "execution_task_id", insertable = false, updatable = false)
    @JsonIgnore
    private TaskExecution taskExecution;

    @Column(name = "execution_task_id")
    @EqualsAndHashCode.Include
    @Id
    @ToString.Include
    private UUID taskExecutionId;

    @Transient
    private Integer duration;

    @Transient
    @Schema(hidden = true)
    private byte points;

    @Column(columnDefinition = "TEXT")
    private String reply;

    @Transient
    private byte[] drawing;

    private Language language;

    private ActionItemType itemType;


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


    public ExecutedItem(UUID userId, UUID itemId, UUID taskExecutionId, Integer duration, String reply, byte[] drawing, Language language, ActionItemType actionItemType) {
        this.userId = userId;
        this.itemId = itemId;
        this.taskExecutionId = taskExecutionId;
        this.duration = duration;
        this.reply = reply;
        this.drawing = drawing;
        this.language = language;
        this.itemType = actionItemType;
    }

}

