package com.lehit.programs.model.projection;


import com.lehit.programs.model.enums.ContentVisibilityStatus;

import java.util.UUID;


public interface ProgramWithAuthorProjection {
    UUID getId();

    String getTitle();

    String getAvatarUrl();

    String getDescription();

    AuthorSimpleDataProjection getAuthor();

    ContentVisibilityStatus getVisibilityStatus();

}
