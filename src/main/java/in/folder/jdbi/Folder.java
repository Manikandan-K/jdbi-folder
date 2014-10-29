package in.folder.jdbi;

import in.folder.jdbi.mapper.AnnotatedField1;
import in.folder.jdbi.mapper.AnnotatedFieldFactory1;
import in.folder.jdbi.mapper.AnnotatedFields1;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.hyphen.Hyphen.where;
import static in.folder.jdbi.mapper.FieldHelper.get;
import static java.util.Objects.isNull;

public class Folder<T> {

    private Map<Class<?>, AnnotatedFields1> fieldsMap = new HashMap<>();

    public void fold(List<T> accumulator, T currentObject) {
        fieldsMap = AnnotatedFieldFactory1.get(currentObject.getClass());
        mergeCollection(accumulator, currentObject);
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

    private<M> void mergeCollection(Collection<M> collection, M currentObject) {
        Object alreadyPresentValue = getAlreadyPresentValue(collection, currentObject);

        if(isNull(alreadyPresentValue) ) {
            collection.add(currentObject);
        }else {
            mergeObject(alreadyPresentValue, currentObject);
        }
    }

    private <M> void handleOneToMany(Object oldObject, Object newObject) {
        if (oldObject instanceof Collection && newObject instanceof Collection) {
            Collection<M> oldCollection = (Collection<M>) oldObject;
            Collection<M> newCollection = (Collection<M>) newObject;

            if (newCollection.iterator().hasNext()) {
                mergeCollection(oldCollection, newCollection.iterator().next());
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
