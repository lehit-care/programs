package com.lehit.programs.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lehit.programs.model.enums.ContentVisibilityStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;


@NamedEntityGraph(
        name = "program-including-tasks",
        attributeNodes = {
                @NamedAttributeNode(value = "tasks")
        }
)


@NamedEntityGraph(
        name = "program-including-author",
        attributeNodes = {
                @NamedAttributeNode(value = "author")
        }
)

@Entity
@Getter @Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor @AllArgsConstructor
@Builder @ToString(onlyExplicitlyIncluded = true)
public class Program implements Serializable {
    @Serial
    @Transient
    private static final long serialVersionUID = 7856526077883283627L;

    @Id
    @Column(updatable= false)
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @EqualsAndHashCode.Include
    @ToString.Include
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID id;

    @NotBlank(message = "Title is mandatory")
    private String title;

    private String avatarUrl;

    @Column(columnDefinition = "TEXT")
    private String description;


    @JsonIgnore
    @OneToMany(mappedBy = "program", orphanRemoval = true)
    @OrderBy("position")
    private List<Task> tasks;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", insertable = false, updatable = false)
    private Author author;

    @Column(name = "author_id")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID authorId;

    public ContentVisibilityStatus visibilityStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", insertable = false, updatable = false)
    @JsonIgnore
    private Category category;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "category_id")
    private UUID categoryId;
}
