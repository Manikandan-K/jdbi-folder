package com.github.rkmk;

import com.github.rkmk.mapper.AnnotatedField;
import com.github.rkmk.mapper.AnnotatedFieldFactory;
import com.github.rkmk.mapper.AnnotatedFields;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.github.rkmk.Hyphen.where;
import static com.github.rkmk.mapper.FieldHelper.get;
import static java.util.Objects.isNull;

public class Folder<T> {

    private Map<Class<?>, AnnotatedFields> fieldsMap = new HashMap<>();

    public void fold(List<T> accumulator, T currentObject) {
        fieldsMap = AnnotatedFieldFactory.get(currentObject.getClass());
        mergeCollection(accumulator, currentObject);
    }

    private void mergeObject(Object oldObject, Object newObject) {
        if(isNull(oldObject) || isNull(newObject))
            return;

        AnnotatedFields annotatedFields = fieldsMap.get(oldObject.getClass());

        for (AnnotatedField annotatedField : annotatedFields.values()) {
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
        for (AnnotatedField primaryKeyField : fieldsMap.get(object.getClass()).getPrimaryKeys()) {
            Field field = primaryKeyField.getField();
            filter.put(primaryKeyField.getName(), get(field, object));
        }
        Collection<M> result = where(collection, filter);
        return result.size() > 0 ? result.iterator().next() : null;
    }
}
