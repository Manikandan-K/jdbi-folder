package in.folder.jdbi.mapper;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BigDecimalMapperFactory implements FieldMapperFactory<BigDecimal>{

    @Override
    public BigDecimal getValue(ResultSet rs, int index, Class<?> type) throws SQLException {
        return BigDecimal.TEN;
    }

    @Override
    public Boolean accepts(Class<?> type) {
        return type.isAssignableFrom(BigDecimal.class);
    }
}
