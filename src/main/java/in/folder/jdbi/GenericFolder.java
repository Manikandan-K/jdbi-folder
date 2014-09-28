package in.folder.jdbi;

import in.folder.jdbi.helper.FieldHelper;
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

    public GenericFolder(Class<T> type) {
        this.type = type;
        mapper = new CustomMapper<>(type);
        acc = new ArrayList<>();
        processFields(type, fieldsMap);
    }

    public List<T> getAccumulator(){
        return acc;
    }

    @Override
    public List<T> fold(List<T> accumulator, ResultSet rs, StatementContext ctx) throws SQLException {
        T object = mapper.map(rs.getRow(), rs, ctx);
        T alreadyPresentObject = getValue(accumulator, object, type);
        if(alreadyPresentObject != null) {
            accumulator.remove(alreadyPresentObject);
            object = alreadyPresentObject;
        }

        List<String> resultFieldNames = getAllResultFieldNames(rs);

        for (String childClassName : getChildClassNames(rs)) {
            AnnotatedField annotatedField = fieldsMap.get(type).get(childClassName);
            if(annotatedField.isOneToMany()) {
                handleOneToMany(rs, ctx, object, resultFieldNames, childClassName, annotatedField);
            }else if(annotatedField.isOneToOne()) {
                handleOneToOne(rs, ctx, object, resultFieldNames, childClassName, annotatedField);
            }
        }
        accumulator.add(object);
        return accumulator;
    }

    private void handleOneToOne(ResultSet rs, StatementContext ctx, T object, List<String> resultFieldNames, String childClassName, AnnotatedField annotatedField) throws SQLException {
        if( isChildRowPresent(rs, resultFieldNames, childClassName) ) {
            Field field = annotatedField.getField();
            Object nestedObject = annotatedField.getMapper().map(rs.getRow(), rs, ctx);
            FieldHelper.set(field, object, nestedObject);
        }
    }

    private void handleOneToMany(ResultSet rs, StatementContext ctx, T object, List<String> resultFieldNames, String childClassName, AnnotatedField annotatedField) throws SQLException {
        Field field = annotatedField.getField();

        Collection<Object> childObjectList= FieldHelper.get(field, object) == null ? new ArrayList<>() : (Collection<Object>) FieldHelper.get(field, object);
        if( isChildRowPresent(rs, resultFieldNames, childClassName) ) {
            Object childObject = annotatedField.getMapper().map(rs.getRow(), rs, ctx);
            if(getValue(childObjectList, childObject, annotatedField.getReturnType()) == null) {
                childObjectList.add(childObject);
            }
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

    private<M> M getValue(Collection<M> collection, M object, Class<?> type) {
        HashMap<String, Object> filter = new HashMap<>();
        for (AnnotatedField primaryKeyField : fieldsMap.get(type).getPrimaryKeys()) {
            Field field = primaryKeyField.getField();
            filter.put(primaryKeyField.getName(), FieldHelper.get(field,object));
        }
        Collection<M> result = where(collection, filter);
        return result.size() > 0 ? result.iterator().next() : null;
    }
}
