package in.folder.jdbi.mapper;

import in.folder.jdbi.annotations.OneToMany;
import in.folder.jdbi.annotations.OneToOne;
import in.folder.jdbi.helper.FieldHelper;

import java.lang.reflect.Field;
import java.util.*;

public class AnnotatedField1 {
    private Field field;
    private Class<?> type;

    public AnnotatedField1(Field field, Class<?> type) {
        this.field = field;
        this.type = type;
    }

    public void set(Object object, Object value) {
        if(type.equals(OneToOne.class)) {
            FieldHelper.set(field, object, value);
        }else if(type.equals(OneToMany.class)) {
            FieldHelper.set(field, object, getContainerValue(value));
        }
    }

    private Collection<Object> getContainerValue(Object value) {
        Collection<Object> collection = new ArrayList<>();
        if(Set.class.isAssignableFrom(field.getType())) {
            collection = new HashSet<>();
        }
        collection.add(value);
        return collection;
    }
}
