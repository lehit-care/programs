package com.lehit.programs.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Getter @Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor @AllArgsConstructor
@Builder @ToString
public class Author implements Serializable {
    @Schema( accessMode = Schema.AccessMode.READ_ONLY)
    @Id
    @Column(name = "id", updatable= false)
    @EqualsAndHashCode.Include
    private UUID id;

    private String firstName;

    private String lastName;
}
