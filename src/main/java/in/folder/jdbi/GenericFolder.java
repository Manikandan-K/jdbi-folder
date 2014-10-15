package in.folder.jdbi;

import in.folder.jdbi.helper.FieldHelper;
import in.folder.jdbi.mapper.CustomMapper;
import in.folder.jdbi.mapper.FieldMapperFactory;
import org.skife.jdbi.v2.Folder2;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;

import static com.hyphen.Hyphen.where;
import static in.folder.jdbi.AnnotatedFieldFactory.processFields;
import static java.util.Objects.isNull;

public class GenericFolder<T> implements Folder2<List<T>> {

    private ResultSetMapper<T> mapper;
    private List<T> acc;
    private Map<Class<?>, AnnotatedFields> fieldsMap = new HashMap<>();
    private Class<T> type;
    private Set<String> childClassNames;
    private List<String> resultFieldNames;
    private static List<FieldMapperFactory> overriddenFactories = new ArrayList<>();

    public GenericFolder(Class<T> type) {
        this.type = type;
        mapper = new CustomMapper<>(type, overriddenFactories);
        acc = new ArrayList<>();
        processFields(type, fieldsMap);
    }

    public List<T> getAccumulator(){
        return acc;
    }

    @Override
    public List<T> fold(List<T> accumulator, ResultSet rs, StatementContext ctx) throws SQLException {
        processResultSet(rs);
        T object = mapper.map(rs.getRow(), rs, ctx);
        object =  addObjectToCollectionIfNeeded(accumulator, object, type);
        mapRelationObject(rs, ctx, object, type);
        return accumulator;
    }

    private void mapRelationObject(ResultSet rs, StatementContext ctx, Object object, Class<?> type) throws SQLException {
        AnnotatedFields annotatedFields = fieldsMap.get(type);

        for (String childClassName : annotatedFields.get().keySet()) {
            if(childClassNames.contains(childClassName)) {
                AnnotatedField annotatedField = annotatedFields.get(childClassName);
                if(annotatedField.isOneToMany()) {
                    handleOneToMany(rs, ctx, object, childClassName, annotatedField);
                }else if(annotatedField.isOneToOne()) {
                    handleOneToOne(rs, ctx, object, childClassName, annotatedField);
                }
            }
        }
    }

    private void handleOneToOne(ResultSet rs, StatementContext ctx, Object object, String childClassName, AnnotatedField annotatedField) throws SQLException {
        if( isChildRowPresent(rs, resultFieldNames, childClassName) ) {
            Field field = annotatedField.getField();
            Object alreadyPresentObject = FieldHelper.get(field, object);
            Object nestedObject = isNull(alreadyPresentObject) ? annotatedField.getMapper().map(rs.getRow(), rs, ctx) : alreadyPresentObject;
            mapRelationObject(rs, ctx, nestedObject, annotatedField.getReturnType());
            FieldHelper.set(field, object, nestedObject);
        }
    }

    private void handleOneToMany(ResultSet rs, StatementContext ctx, Object object, String childClassName, AnnotatedField annotatedField) throws SQLException {
        Field field = annotatedField.getField();
        Collection<Object> childObjectList= FieldHelper.get(field, object) == null ? new ArrayList<>() : (Collection<Object>) FieldHelper.get(field, object);

        if( isChildRowPresent(rs, resultFieldNames, childClassName) ) {
            Object childObject = annotatedField.getMapper().map(rs.getRow(), rs, ctx);
            childObject = addObjectToCollectionIfNeeded(childObjectList, childObject, annotatedField.getReturnType());
            mapRelationObject(rs, ctx, childObject, annotatedField.getReturnType());
        }
        FieldHelper.set(field, object, childObjectList);
    }

    private boolean isChildRowPresent(ResultSet rs, List<String> fieldNames, String childClassName) throws SQLException {
        for (String fieldName : fieldNames) {
             if( fieldName.contains(childClassName+"$") && !isNull(rs.getObject(fieldName)) ) {
                return true;
             }
        }
        return false;
    }

    private void processResultSet(ResultSet rs) throws SQLException {
        this.childClassNames = getChildClassNames(rs);
        this.resultFieldNames = getAllResultFieldNames(rs);
    }

    private Set<String> getChildClassNames(ResultSet rs) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();
        Set<String> childClassNames = new HashSet<>();

        for (int i = 1; i <= metaData.getColumnCount(); i++) {
            String name = metaData.getColumnLabel(i).toLowerCase();
            String[] split = name.split("\\$");
            for (int j=split.length; j > 1 ; j--) {
                childClassNames.add(split[j-2]);
            }
        }
        return childClassNames;
    }

    private List<String> getAllResultFieldNames(ResultSet rs) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();
        List<String> fieldNames = new ArrayList<>();

        for (int i = 1; i <= metaData.getColumnCount(); i++) {
            fieldNames.add(metaData.getColumnLabel(i).toLowerCase());
        }
        return fieldNames;
    }

    /*
        If the object already present in the collection, current object will not be added to the collection.
        And returns already present object. If the object is not present in the collection and it will be added to the collection
        and current object will be returned.
    */
    public <M> M addObjectToCollectionIfNeeded(Collection<M> collection, M object, Class<?> type){
        M alreadyPresentObject = getAlreadyPresentValue(collection, object, type);
        if(isNull(alreadyPresentObject)) {
            collection.add(object);
            return object;
        }
        return alreadyPresentObject;
    }

    private<M> M getAlreadyPresentValue(Collection<M> collection, M object, Class<?> type) {
        HashMap<String, Object> filter = new HashMap<>();
        for (AnnotatedField primaryKeyField : fieldsMap.get(type).getPrimaryKeys()) {
            Field field = primaryKeyField.getField();
            filter.put(primaryKeyField.getName(), FieldHelper.get(field,object));
        }
        Collection<M> result = where(collection, filter);
        return result.size() > 0 ? result.iterator().next() : null;
    }

    public static void register(FieldMapperFactory factory) {
        overriddenFactories.add(factory);
    }

    static List<FieldMapperFactory> getOverriddenFactories(){
        return overriddenFactories;
    }

}
