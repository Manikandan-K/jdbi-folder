package in.folder.jdbi.helper;

import java.lang.reflect.Field;

public class FieldHelper {

    public static void set(Field field, Object object, Object value) {
        field.setAccessible(true);

        try {
            field.set(object, value);
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException(String.format("The field %s is not accessible",field.getName()), e);
        }
    }

    public static Object get(Field field, Object object) {
        field.setAccessible(true);

        try {
            return field.get(object);
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException(String.format("The field %s is not accessible",field.getName()), e);
        }
    }
}
