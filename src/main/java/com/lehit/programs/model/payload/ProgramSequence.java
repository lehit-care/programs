package com.lehit.programs.model.payload;

import java.util.List;
import java.util.UUID;

public record ProgramSequence(List<ExecutableEntitySequence> sequences) {

    public record ExecutableEntitySequence(UUID id, int position){}
}
