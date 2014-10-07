package in.folder.jdbi;

import in.folder.jdbi.annotations.OneToMany;
import in.folder.jdbi.annotations.OneToOne;
import in.folder.jdbi.annotations.PrimaryKey;
import in.folder.jdbi.helper.FieldHelper;

import java.lang.reflect.Field;
import java.util.Map;

public class AnnotatedFieldFactory {


    public static void processFields(Class<?> type, Map<Class<?>, AnnotatedFields> fieldsMap) {
        AnnotatedFields annotatedFields = new AnnotatedFields();

        for (Field field : type.getDeclaredFields()) {
            AnnotatedField annotatedField = AnnotatedFieldFactory.create(field);
            annotatedFields.add(annotatedField);
            if(annotatedField != null && annotatedField.isNestedField())  {
                processFields(annotatedField.getReturnType(), fieldsMap);
            }
        }
        fieldsMap.put(type, annotatedFields);
    }


    public static AnnotatedField create(Field field) {
        AnnotatedField annotatedField = null;
        if(field.isAnnotationPresent(OneToOne.class)) {
            annotatedField = createForOneToOne(field);
        } else if(field.isAnnotationPresent(OneToMany.class)) {
            annotatedField = createForOneToMany(field);
        }else if(field.isAnnotationPresent(PrimaryKey.class)) {
            annotatedField = createForPrimaryKey(field);
        }

        return annotatedField;
    }

    private static AnnotatedField createForOneToOne(Field field) {
        OneToOne annotation = field.getAnnotation(OneToOne.class);

        String name = annotation.name().toLowerCase();
        return new AnnotatedField(OneToOne.class, field, field.getType(), name);
    }

    private static AnnotatedField createForOneToMany(Field field) {
        OneToMany annotation = field.getAnnotation(OneToMany.class);
        String name = annotation.name().toLowerCase();
        return new AnnotatedField(OneToMany.class, field, FieldHelper.getParameterisedReturnType(field), name);
    }

    private static AnnotatedField createForPrimaryKey(Field field) {
        return new AnnotatedField(PrimaryKey.class, field, field.getType(), field.getName());
    }

}
