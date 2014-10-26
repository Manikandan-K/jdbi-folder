package in.folder.jdbi.mapper;

import in.folder.jdbi.annotations.ColumnName;
import in.folder.jdbi.annotations.OneToMany;
import in.folder.jdbi.annotations.OneToOne;
import in.folder.jdbi.annotations.PrimaryKey;
import in.folder.jdbi.helper.FieldHelper;
import in.folder.jdbi.helper.FieldWrapper;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static in.folder.jdbi.helper.FieldHelper.getParameterisedReturnType;
import static java.util.Objects.nonNull;

public class AnnotatedFieldFactory1 {

    private static ConcurrentHashMap<Class<?>, AnnotatedFields1> annotatedFieldsMap = new ConcurrentHashMap<>();
    private static Map<Class<?>, Map<String, FieldWrapper>> fieldsMap = new HashMap<>();

    public static ConcurrentHashMap<Class<?>, AnnotatedFields1> get(Class<?> type) {
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
        processFields(type, "", fields);
        fieldsMap.put(type, fields);
    }

    private static AnnotatedField1 create(Field field) {
        AnnotatedField1 annotatedField = null;

        if(field.isAnnotationPresent(OneToOne.class)) {
            annotatedField = new AnnotatedField1(field, OneToOne.class, field.getType());
        } else if(field.isAnnotationPresent(OneToMany.class)) {
            annotatedField = new AnnotatedField1(field, OneToMany.class, getParameterisedReturnType(field));
        }else if(field.isAnnotationPresent(PrimaryKey.class)) {
            annotatedField = new AnnotatedField1(field, PrimaryKey.class, field.getType());
        }

        return annotatedField;
    }

    private static void processFields(Class<?> type, String nameSpace, Map<String, FieldWrapper> fields) {
        AnnotatedFields1 annotatedFields = new AnnotatedFields1();

        for (Field field : FieldHelper.getFields(type)) {
            AnnotatedField1 annotatedField = create(field);
            annotatedFields.add(annotatedField);
            if(annotatedField != null && annotatedField.isNestedField())  {
                processFields(annotatedField.getReturnType(), annotatedField.getNameSpace(), fields);
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
