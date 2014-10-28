package in.folder.jdbi.mapper;


import in.folder.jdbi.annotations.ColumnName;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.*;

import static java.util.Objects.nonNull;

public class CustomMapper<T> implements ResultSetMapper<T>
{
    private final Class<T> type;
    private final Map<String, Field> fields = new HashMap<>();
    private String appendText = "";
    private List<FieldMapperFactory> factories = new ArrayList<>();

    public CustomMapper(Class<T> type) {
        this(type, new ArrayList<>());
    }

    public CustomMapper(Class<T> type, List<FieldMapperFactory> overriddenFactories) {
        this.type = type;
        for (Field field : getFields(type)) {
            ColumnName annotation = field.getAnnotation(ColumnName.class);
            String name = nonNull(annotation) ? annotation.value().toLowerCase() : field.getName().toLowerCase();
            fields.put(name, field);
        }

        this.factories.addAll(overriddenFactories);
        this.factories.addAll(new FieldMapperFactories().getValues());
    }

    public CustomMapper(Class<T> type, String appendText, List<FieldMapperFactory> overriddenFactories) {
        this(type, overriddenFactories);
        this.appendText = appendText;
    }

    private List<Field> getFields(Class<?> type) {
        List<Field> result = new ArrayList<>();
        Class<?> clazz = type;
        while(clazz.getSuperclass() != null) {
            result.addAll(Arrays.asList(clazz.getDeclaredFields()));
            clazz = clazz.getSuperclass();
        }
        return result;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public T map(int row, ResultSet rs, StatementContext ctx)
            throws SQLException
    {
        T object;
        try {
            object = type.newInstance();
        }
        catch (Exception e) {
            throw new IllegalArgumentException(String.format("A bean, %s, was mapped " +
                    "which was not instantiable", type.getName()), e);
        }

        ResultSetMetaData metadata = rs.getMetaData();

        for (int index = 1; index <= metadata.getColumnCount(); ++index) {
            String name = metadata.getColumnLabel(index).toLowerCase().replace("_", "").replace(appendText, "");

            Field field = fields.get(name);

            if (field != null) {
                Class type = field.getType();
                Object value = null;

                for (FieldMapperFactory factory : factories) {
                    if(factory.accepts(type)) {
                        value = factory.getValue(rs, index, type);
                        break;
                    }
                }

                FieldHelper.set(field, object, value);
            }
        }

        return object;
    }
}


