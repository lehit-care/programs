package com.lehit.programs.model.projection;


import com.lehit.programs.model.ActionItem;

import java.util.List;
import java.util.UUID;


public interface TaskWithItemsProjection {
      UUID getId();

       String getTitle();

       String getDescription();

       String getAvatarUrl();

       int getPosition();

       List<ActionItem> getActionItems();

       UUID getProgramId();
}
