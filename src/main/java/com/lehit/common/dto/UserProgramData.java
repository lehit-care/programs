package com.lehit.common.dto;

import jakarta.persistence.Transient;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;


@Getter @Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@Builder @ToString
public class UserProgramData implements Serializable {
    @Serial
    @Transient
    private static final long serialVersionUID = 7175526077883281627L;

    private UUID id;


    private UUID programId;
}
