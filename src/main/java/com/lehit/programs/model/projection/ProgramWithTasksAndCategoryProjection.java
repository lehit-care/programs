package com.lehit.programs.model.projection;


import com.lehit.programs.model.Category;
import com.lehit.programs.model.Task;
import com.lehit.programs.model.enums.ContentVisibilityStatus;

import java.util.List;
import java.util.UUID;


public interface ProgramWithTasksAndCategoryProjection {
    UUID getId();

    String getTitle();

    String getAvatarUrl();

    String getDescription();

    UUID getAuthorId();

    Category getCategory();

    List<Task> getTasks();

    ContentVisibilityStatus getVisibilityStatus();

}
