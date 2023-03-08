package com.lehit.programs.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lehit.common.enums.Language;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import java.util.UUID;


@Entity
@Getter @Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor @AllArgsConstructor
@Builder @ToString
public class Category implements Serializable {

    @Id
    @Column(updatable= false)
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @EqualsAndHashCode.Include
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID id;

    @Column(unique = true)
    private String name;

    @Column(nullable = false)
    private Language language;
}
