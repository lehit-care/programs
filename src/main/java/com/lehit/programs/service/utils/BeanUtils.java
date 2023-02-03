package com.lehit.programs.service.utils;

import org.springframework.data.util.ReflectionUtils;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.Map;

@Service
public class BeanUtils {

     public<T> void updateFields(Map<String, Object> fields, T objectInstance){

        fields.forEach((k, v) -> {
            Field field = org.springframework.util.ReflectionUtils.findField(objectInstance.getClass(), k);
            field.setAccessible(true);
            ReflectionUtils.setField(field, objectInstance, v);
        });
    }
}
