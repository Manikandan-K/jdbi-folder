package in.folder.jdbi.mapper;

import in.folder.jdbi.annotations.OneToMany;
import in.folder.jdbi.annotations.OneToOne;
import in.folder.jdbi.annotations.PrimaryKey;

import java.lang.reflect.Field;
import java.util.*;

public class AnnotatedField {
    private Field field;
    private Class<?> annotationType;
    private Class<?> type;
    private String nameSpace;


    public AnnotatedField(Field field, Class<?> annotationType, Class<?> type) {
        this.field = field;
        this.annotationType = annotationType;
        this.type = type;
        this.nameSpace = nameSpace();
    }

    public void set(Object object, Object value) {
        if(isOneToOne()) {
            FieldHelper.set(field, object, value);
        }else if(isOneToMany()) {
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

    public Boolean isOneToMany() {
        return annotationType.equals(OneToMany.class);
    }

    public Boolean isOneToOne() {
        return annotationType.equals(OneToOne.class);
    }

    public Boolean isPrimaryKey() {
        return annotationType.equals(PrimaryKey.class);
    }

    public Boolean isNestedField() {
        return isOneToMany() || isOneToOne();
    }

    private String nameSpace() {
        String nameSpace = null ;
        if(isOneToMany()) {
            nameSpace =  field.getAnnotation(OneToMany.class).value().toLowerCase();
        }else if(isOneToOne()) {
            nameSpace = field.getAnnotation(OneToOne.class).value().toLowerCase();
        }
        return nameSpace ;
    }

    public String getNameSpace() {
        return nameSpace;
    }

    public Class<?> getType() {
        return type;
    }

    public Field getField() {
        return field;
    }

    public String getName() {
        return field.getName();
    }

    public Class<?> getDeclaringClass() {
        return field.getDeclaringClass();
    }
}
