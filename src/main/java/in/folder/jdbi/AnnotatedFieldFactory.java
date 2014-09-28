package in.folder.jdbi;

import in.folder.jdbi.annotations.OneToMany;
import in.folder.jdbi.annotations.OneToOne;

import java.lang.reflect.Field;

public class AnnotatedFieldFactory {

    public static AnnotatedField createForOneToOne(Field field) {
        OneToOne annotation = field.getAnnotation(OneToOne.class);

        String name = annotation.name().toLowerCase();
        CustomMapper<?> mapper = new CustomMapper<>(field.getType(), name + "$");
        return new AnnotatedField(OneToOne.class, field, mapper, name);
    }

    public static AnnotatedField createForOneToMany(Field field) {
        OneToMany annotation = field.getAnnotation(OneToMany.class);

        String name = annotation.name().toLowerCase();
        CustomMapper<?> mapper = new CustomMapper<>(annotation.type(), name + "$");
        return new AnnotatedField(OneToMany.class, field, mapper, name);
    }
}
