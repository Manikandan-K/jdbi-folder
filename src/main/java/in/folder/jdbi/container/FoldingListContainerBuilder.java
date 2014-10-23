package in.folder.jdbi.container;

import in.folder.jdbi.mapper.AnnotatedField1;
import in.folder.jdbi.mapper.AnnotatedFieldFactory1;
import in.folder.jdbi.mapper.AnnotatedFields1;
import org.skife.jdbi.v2.ContainerBuilder;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static com.hyphen.Hyphen.where;
import static in.folder.jdbi.helper.FieldHelper.get;
import static java.util.Objects.isNull;

public class FoldingListContainerBuilder implements ContainerBuilder<FoldingList<?>>{

    private final FoldingList<Object> list;
    private Map<Class<?>, AnnotatedFields1> fieldsMap = new HashMap<>();

    public FoldingListContainerBuilder() {
        this.list = new FoldingList<>();
    }

    @Override
    public ContainerBuilder<FoldingList<?>> add(Object currentObject) {
        fieldsMap = AnnotatedFieldFactory1.get(currentObject.getClass());

        Object alreadyPresentValue = getAlreadyPresentValue(list.getValues(), currentObject);

        if( isNull(alreadyPresentValue) ) {
            list.add(currentObject);
        }else {
            mergeObject(alreadyPresentValue, currentObject);
        }
        return this;
    }

    @Override
    public FoldingList<?> build() {
        return list;
    }

    private void mergeObject(Object oldObject, Object newObject) {
        if(isNull(oldObject) || isNull(newObject))
            return;

        AnnotatedFields1 annotatedFields = fieldsMap.get(oldObject.getClass());

        for (AnnotatedField1 annotatedField : annotatedFields.get()) {
            if(annotatedField.isOneToOne()) {
                mergeObject(get(annotatedField.getField(), oldObject), get(annotatedField.getField(), newObject));
            }else if(annotatedField.isOneToMany()) {
                handleOneToMany(oldObject, newObject, annotatedField.getField());
            }
        }
    }

    private void handleOneToMany(Object oldObject, Object newObject, Field field) {
        Object oldValue = get(field, oldObject);
        Object newValue = get(field, newObject);

        if(oldValue instanceof Collection && newValue instanceof Collection) {
            Collection<Object> oldCollection = (Collection<Object>) oldValue;
            Collection<Object> newCollection = (Collection<Object>) newValue;

            Object currentObject = newCollection.iterator().next();
            Object alreadyPresentValue = getAlreadyPresentValue(oldCollection, currentObject);

            if( isNull(alreadyPresentValue) ) {
                oldCollection.add(currentObject);
            }else {
                mergeObject(alreadyPresentValue, currentObject);
            }
        }
    }

    private<M> M getAlreadyPresentValue(Collection<M> collection, M object) {
        HashMap<String, Object> filter = new HashMap<>();
        for (AnnotatedField1 primaryKeyField : fieldsMap.get(object.getClass()).getPrimaryKeys()) {
            Field field = primaryKeyField.getField();
            filter.put(primaryKeyField.getName(), get(field, object));
        }
        Collection<M> result = where(collection, filter);
        return result.size() > 0 ? result.iterator().next() : null;
    }

}
