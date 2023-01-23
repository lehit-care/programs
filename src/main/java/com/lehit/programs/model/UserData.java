package com.lehit.programs.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

@NamedEntityGraph(
        name = "including-program",
        attributeNodes = {
                @NamedAttributeNode("program")
        }
)

@Entity
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@Builder
@ToString(onlyExplicitlyIncluded = true) @EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class UserData  implements Serializable {
    @Serial
    @Transient
    private static final long serialVersionUID = 6656526077993581627L;


    @Id
    @Column(updatable = false)
    @ToString.Include
    @EqualsAndHashCode.Include
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "program_id", insertable = false, updatable = false)
    private Program program;

    @Column(name = "program_id")
    @ToString.Include
    private UUID programId;

}
