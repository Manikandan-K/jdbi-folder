package in.folder.jdbi.helper;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import static java.util.Objects.nonNull;

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

    public static Class<?> getParameterisedReturnType(Field field){
        Class<?> result = null ;
        Type genericFieldType = field.getGenericType();
        if(genericFieldType instanceof ParameterizedType){
            ParameterizedType aType = (ParameterizedType) genericFieldType;
            Type[] fieldArgTypes = aType.getActualTypeArguments();
            result = fieldArgTypes.length >0 ? (Class)fieldArgTypes[0] : null ;
        }

        if(nonNull(result)) {
            return result;
        }
        throw new IllegalArgumentException(String.format("The field type %s is not inferrable",field.getName()));
    }
}
