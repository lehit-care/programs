package com.lehit.programs.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;


@NamedEntityGraph(
        name = "including-program",
        attributeNodes = {
                @NamedAttributeNode(value = "program")
        }
)


@Entity
@Getter @Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor @AllArgsConstructor
@Builder @ToString(onlyExplicitlyIncluded = true)
public class Task implements Serializable {
    @Id
    @Column(updatable= false)
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @EqualsAndHashCode.Include
    @ToString.Include
    private UUID id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String avatarUrl;

    @ToString.Include
    private int position;

    @JsonIgnore
    @OneToMany(mappedBy = "task", orphanRemoval = true)
    @OrderBy("position")
    private List<ActionItem> actionItems;


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "program_id", insertable = false, updatable = false)
    @JsonIgnore
    private Program program;

    @Column(name = "program_id")
    @EqualsAndHashCode.Include
    @ToString.Include
    private UUID programId;

}
