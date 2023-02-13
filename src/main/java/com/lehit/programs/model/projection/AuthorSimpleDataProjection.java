package com.lehit.programs.model.projection;

import org.springframework.beans.factory.annotation.Value;

import java.util.UUID;

public interface AuthorSimpleDataProjection {
     UUID getId();

     @Value("#{target.firstName + ' ' + target.lastName}")
     String getTitle();

}
