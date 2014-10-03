package in.folder.jdbi;


import in.folder.jdbi.annotations.OneToMany;
import in.folder.jdbi.annotations.OneToOne;
import in.folder.jdbi.annotations.PrimaryKey;
import in.folder.jdbi.mapper.CustomMapper;

import java.lang.reflect.Field;

public class AnnotatedField {
    private Class<?> type;
    private Field field;
    private Class<?> returnType;
    private CustomMapper<?> mapper;
    private String name;

    public AnnotatedField(Class<?> type, Field field, Class<?> returnType, String name) {
        this.type = type;
        this.field = field;
        if(!isPrimaryKey())  {
            this.returnType = returnType;
            this.mapper = new CustomMapper<>(returnType, name + "$", GenericFolder.getOverriddenFactories());
        }
        this.name = name;
    }

    public Class<?> getReturnType() {
        return returnType;
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

    public Boolean isPrimaryKey() {
        return type.equals(PrimaryKey.class);
    }

    public Boolean isNestedField() {
        return isOneToMany() || isOneToOne();
    }
}
