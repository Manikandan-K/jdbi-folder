package in.folder.jdbi;


import in.folder.jdbi.annotations.OneToMany;
import in.folder.jdbi.annotations.OneToOne;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class AnnotatedField {
    private Class<?> type;
    private Field field;
    private CustomMapper<?> mapper;

    public AnnotatedField(Class<?> type, Field field, CustomMapper<?> mapper) {
        this.type = type;
        this.field = field;
        this.mapper = mapper;
    }

    public Field getField() {
        return field;
    }

    public CustomMapper<?> getMapper() {
        return mapper;
    }

    public Boolean isOneToMany() {
        return type.equals(OneToMany.class);
    }

    public Boolean isOneToOne() {
        return type.equals(OneToOne.class);
    }

    public List<?> defaultValue() {
        return isOneToMany() ? new ArrayList<>() : null;
    }
}
