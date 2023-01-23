package com.lehit.programs.model.pk;

import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Builder
@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
public class ExecutedItemCompositeKey implements Serializable {
    private UUID itemId;
    private UUID taskExecutionId;
}
