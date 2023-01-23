package com.lehit.programs.model;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Transient;
import lombok.*;


import java.io.Serial;
import java.io.Serializable;

@Embeddable
@Builder
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@AttributeOverride( name = "title", column = @Column(name = "summary_title"))
@AttributeOverride( name = "description", column = @Column(name = "summary_description"))
public class TaskSummary implements Serializable {
    @Serial
    @Transient
    private static final long serialVersionUID = 7156526077883281628L;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

}
