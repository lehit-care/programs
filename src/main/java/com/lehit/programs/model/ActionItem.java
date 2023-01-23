package com.lehit.programs.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lehit.programs.model.enums.ActionItemType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;


@Entity
@Getter @Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor @AllArgsConstructor
@Builder @ToString(onlyExplicitlyIncluded = true)
public class ActionItem implements Serializable {
    @Serial
    @Transient
    private static final long serialVersionUID = 3356526077883581627L;

    @Id
    @Column(updatable= false)
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @EqualsAndHashCode.Include
    @ToString.Include
    private UUID id;

    private ActionItemType itemType;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", insertable = false, updatable = false)
    @JsonIgnore
    private Task task;

    @Column(name = "task_id")
    private UUID taskId;

    @ToString.Include
    private int position;

    private String help;

    @Embedded
    private InformationItem informationItem;

    @Embedded
    private QuestionItem questionItem;

    private UUID replyReference;
}
