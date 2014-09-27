package in.folder.jdbi;


import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class CustomMapper<T> implements ResultSetMapper<T>
{
    private final Class<T> type;
    private final Map<String, Field> properties = new HashMap<>();
    private final String appendText;

    public CustomMapper(Class<T> type)
    {
        this(type, "");
    }

    public CustomMapper(Class<T> type, String appendText) {
        this.type = type;
        this.appendText = appendText;
        for (Field field : type.getDeclaredFields()) {
            properties.put(field.getName().toLowerCase(), field);
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public T map(int row, ResultSet rs, StatementContext ctx)
            throws SQLException
    {
        T bean;
        try {
            bean = type.newInstance();
        }
        catch (Exception e) {
            throw new IllegalArgumentException(String.format("A bean, %s, was mapped " +
                    "which was not instantiable", type.getName()), e);
        }

        ResultSetMetaData metadata = rs.getMetaData();

        for (int i = 1; i <= metadata.getColumnCount(); ++i) {
            String name = metadata.getColumnLabel(i).toLowerCase().replace("_", "").replace(appendText, "");

            Field field = properties.get(name);

            if (field != null) {
                Class type = field.getType();

                Object value;

                if (type.isAssignableFrom(Boolean.class) || type.isAssignableFrom(boolean.class)) {
                    value = rs.getBoolean(i);
                }
                else if (type.isAssignableFrom(Byte.class) || type.isAssignableFrom(byte.class)) {
                    value = rs.getByte(i);
                }
                else if (type.isAssignableFrom(Short.class) || type.isAssignableFrom(short.class)) {
                    value = rs.getShort(i);
                }
                else if (type.isAssignableFrom(Integer.class) || type.isAssignableFrom(int.class)) {
                    value = rs.getInt(i);
                }
                else if (type.isAssignableFrom(Long.class) || type.isAssignableFrom(long.class)) {
                    value = rs.getLong(i);
                }
                else if (type.isAssignableFrom(Float.class) || type.isAssignableFrom(float.class)) {
                    value = rs.getFloat(i);
                }
                else if (type.isAssignableFrom(Double.class) || type.isAssignableFrom(double.class)) {
                    value = rs.getDouble(i);
                }
                else if (type.isAssignableFrom(BigDecimal.class)) {
                    value = rs.getBigDecimal(i);
                }
                else if (type.isAssignableFrom(Timestamp.class)) {
                    value = rs.getTimestamp(i);
                }
                else if (type.isAssignableFrom(Time.class)) {
                    value = rs.getTime(i);
                }
                else if (type.isAssignableFrom(java.util.Date.class)) {
                    value = rs.getDate(i);
                }
                else if (type.isAssignableFrom(String.class)) {
                    value = rs.getString(i);
                }
                else if (type.isEnum()) {
                    value = Enum.valueOf(type, rs.getString(i));
                }
                else {
                    value = rs.getObject(i);
                }

                if (rs.wasNull() && !type.isPrimitive()) {
                    value = null;
                }

                try {
                    field.setAccessible(true);
                    field.set(bean, value);
                } catch (IllegalAccessException e) {
                    throw new IllegalArgumentException(String.format("Unable to access the property, %s", name), e);
                }

            }
        }

        return bean;
    }
}


