package in.folder.jdbi;

import in.folder.jdbi.annotations.OneToMany;
import org.skife.jdbi.v2.Folder2;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;

import static java.util.Objects.isNull;
import static java.util.stream.Collectors.toList;

public class GenericFolder<T> implements Folder2<List<T>> {

    private ResultSetMapper<T> mapper;
    private List<T> acc;

    public GenericFolder(Class<T> type) {
        this.mapper = new CustomMapper<>(type);
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

        List<Field> oneToManyFields = getOneToManyFields(object);
        Set<String> childClassNames = getChildClassNames(rs);
        List<String> resultFieldNames = getAllResultFieldNames(rs);

        for (String childClassName : childClassNames) {
            Field field = oneToManyFields.stream().filter(o -> o.getAnnotation(OneToMany.class).name().equalsIgnoreCase(childClassName)).collect(toList()).get(0);
            field.setAccessible(true);
            Class<?> childType = field.getAnnotation(OneToMany.class).type();

            try {
                Collection<Object> childObject= field.get(object) == null ? new ArrayList<>() : (Collection<Object>) field.get(object);
                if( isChildRowPresent(rs, resultFieldNames, childClassName) ) {
                    CustomMapper<?> childMapper = new CustomMapper<>(childType, childClassName + "$");
                    childObject.add(childMapper.map(rs.getRow(), rs, ctx));
                }
                field.set(object,childObject);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

        }
        accumulator.add(object);

        return accumulator;
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

    private List<Field> getOneToManyFields(T object) {
        List<Field> oneToManyFields = new ArrayList<>();
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field : fields) {
            if(field.isAnnotationPresent(OneToMany.class)) {
                oneToManyFields.add(field);
            }
        }
        return oneToManyFields;
    }

}
