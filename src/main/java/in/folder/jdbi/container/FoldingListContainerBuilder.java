package in.folder.jdbi.container;

import in.folder.jdbi.mapper.AnnotatedField1;
import in.folder.jdbi.mapper.AnnotatedFieldFactory1;
import in.folder.jdbi.mapper.AnnotatedFields1;
import org.skife.jdbi.v2.ContainerBuilder;

import java.lang.reflect.Field;
import java.util.*;

import static com.hyphen.Hyphen.where;
import static in.folder.jdbi.mapper.FieldHelper.get;
import static java.util.Objects.isNull;

public class FoldingListContainerBuilder implements ContainerBuilder<FoldingList<?>>{

    private final List<Object> list;
    private Map<Class<?>, AnnotatedFields1> fieldsMap = new HashMap<>();

    public FoldingListContainerBuilder() {
        this.list = new ArrayList<>();
    }

    @Override
    public ContainerBuilder<FoldingList<?>> add(Object currentObject) {
        fieldsMap = AnnotatedFieldFactory1.get(currentObject.getClass());
        mergeCollection(list, currentObject);
        return this;
    }

    @Override
    public FoldingList<?> build() {
        return new FoldingList<>(list);
    }

    private void mergeObject(Object oldObject, Object newObject) {
        if(isNull(oldObject) || isNull(newObject))
            return;

        AnnotatedFields1 annotatedFields = fieldsMap.get(oldObject.getClass());

        for (AnnotatedField1 annotatedField : annotatedFields.values()) {
            Object oldValue = get(annotatedField.getField(), oldObject);
            Object newValue = get(annotatedField.getField(), newObject);
            if(annotatedField.isOneToOne()) {
                mergeObject(oldValue, newValue);
            }else if(annotatedField.isOneToMany()) {
                handleOneToMany(oldValue, newValue);
            }
        }
    }

    private void handleOneToMany(Object oldObject, Object newObject) {
        if (oldObject instanceof Collection && newObject instanceof Collection) {
            Collection<Object> oldCollection = (Collection<Object>) oldObject;
            Collection<Object> newCollection = (Collection<Object>) newObject;

            if (newCollection.iterator().hasNext()) {
                mergeCollection(oldCollection, newCollection.iterator().next());
            }
        }
    }

    private void mergeCollection(Collection<Object> collection, Object currentObject) {
        Object alreadyPresentValue = getAlreadyPresentValue(collection, currentObject);

        if( isNull(alreadyPresentValue) ) {
            collection.add(currentObject);
        }else {
            mergeObject(alreadyPresentValue, currentObject);
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
