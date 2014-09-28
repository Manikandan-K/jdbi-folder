package in.folder.jdbi;


import in.folder.jdbi.annotations.OneToMany;
import in.folder.jdbi.annotations.OneToOne;

import java.lang.reflect.Field;

public class AnnotatedField {
    private Class<?> type;
    private Field field;
    private CustomMapper<?> mapper;
    private String name;

    public AnnotatedField(Class<?> type, Field field, CustomMapper<?> mapper, String name) {
        this.type = type;
        this.field = field;
        this.mapper = mapper;
        this.name = name;
    }

    public Field getField() {
        return field;
    }

    public CustomMapper<?> getMapper() {
        return mapper;
    }

    public String getName() {
        return name;
    }

    public Boolean isOneToMany() {
        return type.equals(OneToMany.class);
    }

    public Boolean isOneToOne() {
        return type.equals(OneToOne.class);
    }
}
