package in.folder.jdbi.mapper;


import in.folder.jdbi.annotations.ColumnName;
import in.folder.jdbi.helper.FieldHelper;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Objects.nonNull;

public class CustomMapper<T> implements ResultSetMapper<T>
{
    private final Class<T> type;
    private final Map<String, Field> fields = new HashMap<>();
    private final String appendText;
    private List<FieldMapperFactory> factories = new ArrayList<>();

    public CustomMapper(Class<T> type, List<FieldMapperFactory> overriddenFactories) {
        this(type, "", overriddenFactories);
    }

    public CustomMapper(Class<T> type, String appendText, List<FieldMapperFactory> overriddenFactories) {
        this.type = type;
        this.appendText = appendText;
        for (Field field : type.getDeclaredFields()) {
            ColumnName annotation = field.getAnnotation(ColumnName.class);
            String name = nonNull(annotation) ? annotation.value().toLowerCase() : field.getName().toLowerCase();
            fields.put(name, field);
        }
        this.factories.addAll(overriddenFactories);
        this.factories.addAll(new FieldMapperFactories().getValues());
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
                Boolean assigned = false;

                for (FieldMapperFactory defaultFactory : factories) {
                    if(defaultFactory.accepts(type)) {
                        value = defaultFactory.getValue(rs, index);
                        assigned = true;
                        break;
                    }
                }

                if(!assigned) {
                    if (type.isEnum() && !assigned) {
                        value = Enum.valueOf(type, rs.getString(index));
                    }
                    else {
                        value = rs.getObject(index);
                    }
                }

                if (rs.wasNull() && !type.isPrimitive()) {
                    value = null;
                }

                FieldHelper.set(field, object, value);
            }
        }

        return object;
    }
}


