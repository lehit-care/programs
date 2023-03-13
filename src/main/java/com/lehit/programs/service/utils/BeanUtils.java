package com.lehit.programs.service.utils;

import com.lehit.programs.model.ActionItem;
import com.lehit.programs.model.InformationItem;
import org.apache.http.util.Asserts;
import org.springframework.data.util.ReflectionUtils;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.Map;

@Service
public class BeanUtils {

    public<T> void updateFields(Map<String, Object> fields, T objectInstance){

        fields.forEach((k, v) -> {
            if(isNested(k))
                updateNestedField(k, v, objectInstance);
            else
                updateBasicField(k, v, objectInstance);
        });
    }


    private<T> void updateBasicField(String fieldName, Object value, T objectInstance){
        Field field = org.springframework.util.ReflectionUtils.findField(objectInstance.getClass(), fieldName);
        Asserts.check(field != null, "Non existing field.");
        field.setAccessible(true);
        ReflectionUtils.setField(field, objectInstance, value);
    }

    //    todo make universal(support more dept)
    private<T> void updateNestedField(String fieldName, Object value, T objectInstance){

        var nestedFields = fieldName.split("\\.");
        var nestedClass = org.springframework.util.ReflectionUtils.findField(objectInstance.getClass(), nestedFields[0]).getType();
        Field field = org.springframework.util.ReflectionUtils.findField(nestedClass, nestedFields[1]);

        Asserts.check(field != null, "Non existing field.");
        field.setAccessible(true);
        Object finObject =InformationItem.class.equals(nestedClass)? ((ActionItem)objectInstance).getInformationItem(): ((ActionItem)objectInstance).getQuestionItem();

        ReflectionUtils.setField(field,finObject, value);
    }

    private boolean isNested(String fieldName){
        return fieldName.contains(".");
    }

}
