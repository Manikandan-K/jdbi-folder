package com.github.rkmk.mapper;

import com.github.rkmk.annotations.ColumnName;
import com.github.rkmk.annotations.OneToMany;
import com.github.rkmk.annotations.OneToOne;
import com.github.rkmk.annotations.PrimaryKey;
import com.github.rkmk.helper.FieldWrapper;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.github.rkmk.helper.FieldWrapper.rootClassNameSpace;
import static com.github.rkmk.mapper.FieldHelper.getParameterisedReturnType;
import static java.util.Objects.nonNull;

public class AnnotatedFieldFactory {

    private static ConcurrentHashMap<Class<?>, AnnotatedFields> annotatedFieldsMap = new ConcurrentHashMap<>();
    private static Map<Class<?>, Map<String, FieldWrapper>> fieldsMap = new HashMap<>();

    public static ConcurrentHashMap<Class<?>, AnnotatedFields> get(Class<?> type) {
        if(!annotatedFieldsMap.containsKey(type)) {
            processFields(type);
        }
        return annotatedFieldsMap;
    }

    public static Map<String, FieldWrapper> getFields(Class<?> type) {
        if(!fieldsMap.containsKey(type)) {
            processFields(type);
        }
        return fieldsMap.get(type);
    }


    public static void processFields(Class<?> type) {
        Map<String, FieldWrapper> fields = new HashMap<>();
        processFields(type, rootClassNameSpace, fields);
        fieldsMap.put(type, fields);
    }

    private static AnnotatedField create(Field field) {
        AnnotatedField annotatedField = null;

        if(field.isAnnotationPresent(OneToOne.class)) {
            annotatedField = new AnnotatedField(field, OneToOne.class, field.getType());
        } else if(field.isAnnotationPresent(OneToMany.class)) {
            annotatedField = new AnnotatedField(field, OneToMany.class, getParameterisedReturnType(field));
        }else if(field.isAnnotationPresent(PrimaryKey.class)) {
            annotatedField = new AnnotatedField(field, PrimaryKey.class, field.getType());
        }

        return annotatedField;
    }

    private static void processFields(Class<?> type, String nameSpace, Map<String, FieldWrapper> fields) {
        AnnotatedFields annotatedFields = new AnnotatedFields();

        for (Field field : FieldHelper.getFields(type)) {
            AnnotatedField annotatedField = create(field);
            annotatedFields.add(annotatedField);
            if(annotatedField != null && annotatedField.isNestedField())  {
                processFields(annotatedField.getType(), annotatedField.getNameSpace(), fields);
            }
            processField(fields, nameSpace, field, type);
        }
        annotatedFieldsMap.put(type, annotatedFields);
    }

    private static void processField(Map<String, FieldWrapper> fields, String nameSpace, Field field, Class<?> type) {
        ColumnName annotation = field.getAnnotation(ColumnName.class);
        String name = nonNull(annotation) ? annotation.value() : field.getName();
        fields.put(getResultSetFieldName(nameSpace, name), new FieldWrapper(type, field, nameSpace));
    }

    private static String getResultSetFieldName(String nameSpace, String name) {
        String nameWithoutUnderscore = name.toLowerCase().replace("_", "");
        return nameSpace.isEmpty() ? nameWithoutUnderscore :  nameSpace.toLowerCase() + "$" + nameWithoutUnderscore;
    }

}
