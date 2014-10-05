package in.folder.jdbi.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface FieldMapperFactory<T> {
    public T getValue(ResultSet rs, int index, Class<?> type) throws SQLException;
    public Boolean accepts(Class<?> type);

    public default <T>  T getNullOrValue(Class<?> type, ResultSet rs, T value) throws SQLException {
        return rs.wasNull() && !type.isPrimitive() ? null : value ;
    }

}
