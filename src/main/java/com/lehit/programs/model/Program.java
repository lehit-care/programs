package com.lehit.programs.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lehit.programs.model.enums.ContentVisibilityStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

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
    private UUID id;

    @NotBlank(message = "Title is mandatory")
    private String title;

    private String avatarUrl;

    @Column(columnDefinition = "TEXT")
    private String description;

    @NotNull
    private UUID author;

    @JsonIgnore
    @OneToMany(mappedBy = "program", orphanRemoval = true)
    @OrderBy("position")
    private List<Task> tasks;

    public ContentVisibilityStatus visibilityStatus;

}
