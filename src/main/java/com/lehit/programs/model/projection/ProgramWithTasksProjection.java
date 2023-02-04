package com.lehit.programs.model.projection;


import com.lehit.programs.model.Task;
import com.lehit.programs.model.enums.ContentVisibilityStatus;

import java.util.List;
import java.util.UUID;


public interface ProgramWithTasksProjection {
    UUID getId();

    String getTitle();

    String getAvatarUrl();

    String getDescription();

    UUID getAuthor();

    List<Task> getTasks();

    ContentVisibilityStatus getVisibilityStatus();

}
