package com.lehit.programs.model;

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
public class QuestionItem implements Serializable {
    @Serial
    @Transient
    private static final long serialVersionUID = 7156526077883284848L;

    @Column(columnDefinition = "TEXT")
    private String question;

    @Column(columnDefinition = "TEXT")
    private String replyOptions;
}
