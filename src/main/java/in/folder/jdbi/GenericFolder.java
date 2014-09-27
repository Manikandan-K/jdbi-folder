package in.folder.jdbi;

import in.folder.jdbi.annotations.OneToMany;
import in.folder.jdbi.annotations.OneToOne;
import org.skife.jdbi.v2.Folder2;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;

import static java.util.Objects.isNull;

public class GenericFolder<T> implements Folder2<List<T>> {

    private ResultSetMapper<T> mapper;
    private List<T> acc;
    HashMap<String, AnnotatedField> annotatedFields;

    public GenericFolder(Class<T> type) {
        mapper = new CustomMapper<>(type);
        annotatedFields = getAnnotatedFields(type);
        acc = new ArrayList<>();
    }

    public List<T> getAccumulator(){
        return acc;
    }

    @Override
    public List<T> fold(List<T> accumulator, ResultSet rs, StatementContext ctx) throws SQLException {
        T object = mapper.map(rs.getRow(), rs, ctx);
        if(accumulator.contains(object)) {
            int idx = accumulator.indexOf(object);
            object = accumulator.remove(idx);
        }

        List<String> resultFieldNames = getAllResultFieldNames(rs);

        for (String childClassName : getChildClassNames(rs)) {
            AnnotatedField annotatedField = annotatedFields.get(childClassName);
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
            field.setAccessible(true);
            Object nestedObject = annotatedField.getMapper().map(rs.getRow(), rs, ctx);

            try {
            field.set(object, nestedObject);
            }catch (IllegalAccessException e) {
                e.printStackTrace();
            }

        }

    }

    private void handleOneToMany(ResultSet rs, StatementContext ctx, T object, List<String> resultFieldNames, String childClassName, AnnotatedField annotatedField) throws SQLException {
        Field field = annotatedField.getField();
        field.setAccessible(true);

        try {
            List<Object> childObjectList= field.get(object) == null ? new ArrayList<>() : (List<Object>) field.get(object);
            if( isChildRowPresent(rs, resultFieldNames, childClassName) ) {
                Object childObject = annotatedField.getMapper().map(rs.getRow(), rs, ctx);
                if(!childObjectList.contains(childObject)) {
                    childObjectList.add(childObject);
                }
            }
            field.set(object, childObjectList);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
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

    private HashMap<String, AnnotatedField> getAnnotatedFields(Class<T> type) {
        HashMap<String, AnnotatedField> annotatedFields = new HashMap<>();
        for (Field field : type.getDeclaredFields()) {

            if(field.isAnnotationPresent(OneToMany.class)) {
                OneToMany annotation = field.getAnnotation(OneToMany.class);
                String name = annotation.name().toLowerCase();
                AnnotatedField annotatedField = new AnnotatedField(OneToMany.class,field, new CustomMapper<>(annotation.type(), name + "$"));
                annotatedFields.put(name, annotatedField);
            }else if(field.isAnnotationPresent(OneToOne.class)) {
                OneToOne annotation = field.getAnnotation(OneToOne.class);
                String name = annotation.name().toLowerCase();
                AnnotatedField annotatedField = new AnnotatedField(OneToOne.class,field, new CustomMapper<>(annotation.type(), name + "$"));
                annotatedFields.put(name, annotatedField);
            }
        }
        return annotatedFields;
    }

}
