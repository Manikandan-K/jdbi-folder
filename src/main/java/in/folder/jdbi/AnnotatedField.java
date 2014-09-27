package in.folder.jdbi;


import java.lang.reflect.Field;

public class AnnotatedField {
    private Field field;
    private CustomMapper<?> mapper;

    public AnnotatedField(Field field, CustomMapper<?> mapper) {
        this.field = field;
        this.mapper = mapper;
    }

    public Field getField() {
        return field;
    }

    public CustomMapper<?> getMapper() {
        return mapper;
    }
}
