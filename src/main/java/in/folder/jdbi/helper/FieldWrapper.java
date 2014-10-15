package in.folder.jdbi.helper;

import java.lang.reflect.Field;
import java.util.HashMap;

import static in.folder.jdbi.helper.FieldHelper.set;
import static java.util.Objects.isNull;

public class FieldWrapper {

    private Class<?> classType;
    private Field field;
    private String nameSpace;

    //    public FieldWrapper(Field field, Class<?> annotationType) {
//        this.field = field;
//        this.annotationType = annotationType;
//        this.classType = annotationType.equals(OneToOne.class) ? field.getType() : FieldHelper.getParameterisedReturnType(field);
//    }
//
    public FieldWrapper(Class<?> classType, Field field, String nameSpace) {
        this.classType = classType;
        this.field = field;
        this.nameSpace = nameSpace;
    }

    public Field getField() {
        return field;
    }

    public Class<?> getFieldType() {
        return field.getType();
    }

    public void setValue(Object value, HashMap<String, Object> instanceMap) {
        Object object = instanceMap.get(nameSpace);
        if(isNull(object)) {
            object = FieldHelper.getInstance(classType);
            instanceMap.put(nameSpace, object);
        }
        set(field, object, value);
    }
}
