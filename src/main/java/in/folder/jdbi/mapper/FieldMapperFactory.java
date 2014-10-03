package in.folder.jdbi.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface FieldMapperFactory<T> {
    public T getValue(ResultSet rs, int index) throws SQLException;
    public Boolean accepts(Class<?> type);

}
