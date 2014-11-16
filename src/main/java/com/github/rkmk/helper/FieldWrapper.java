package com.github.rkmk.helper;

import com.github.rkmk.mapper.FieldHelper;

import java.lang.reflect.Field;
import java.util.HashMap;

import static com.github.rkmk.mapper.FieldHelper.set;
import static java.util.Objects.isNull;

public class FieldWrapper {

    private Class<?> classType;
    private Field field;

    public FieldWrapper(Class<?> classType, Field field) {
        this.classType = classType;
        this.field = field;
    }

    public Field getField() {
        return field;
    }

    public Class<?> getFieldType() {
        return field.getType();
    }

    public void setValue(Object value, HashMap<Class<?>, Object> instanceMap) {
        Object object = instanceMap.get(classType);
        if(isNull(object)) {
            object = FieldHelper.getInstance(classType);
            instanceMap.put(classType, object);
        }
        set(field, object, value);
    }
}
