package in.folder.jdbi.mapper;

import in.folder.jdbi.annotations.OneToMany;
import in.folder.jdbi.annotations.OneToOne;
import in.folder.jdbi.annotations.PrimaryKey;

import java.lang.reflect.Field;
import java.util.concurrent.ConcurrentHashMap;

import static in.folder.jdbi.helper.FieldHelper.getFields;
import static in.folder.jdbi.helper.FieldHelper.getParameterisedReturnType;

public class AnnotatedFieldFactory1 {

    private static ConcurrentHashMap<Class<?>, AnnotatedFields1> fieldsMap = new ConcurrentHashMap<>();

    public static ConcurrentHashMap<Class<?>, AnnotatedFields1> get(Class<?> type) {
        if(!fieldsMap.containsKey(type)) {
            processFields(type);
        }
        return fieldsMap;
    }

    public static void processFields(Class<?> type) {
        AnnotatedFields1 annotatedFields = new AnnotatedFields1();

        for (Field field : getFields(type)) {
            AnnotatedField1 annotatedField = create(field);
            annotatedFields.add(annotatedField);
            if(annotatedField != null && annotatedField.isNestedField())  {
                processFields(annotatedField.getReturnType());
            }
        }
        fieldsMap.put(type, annotatedFields);
    }

    public static AnnotatedField1 create(Field field) {
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

}
