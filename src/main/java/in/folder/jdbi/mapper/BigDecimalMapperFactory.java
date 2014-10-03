package in.folder.jdbi.mapper;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BigDecimalMapperFactory implements FieldMapperFactory<BigDecimal>{

    @Override
    public BigDecimal getValue(ResultSet rs, int index) throws SQLException {
        return BigDecimal.TEN;
    }

    public Class<BigDecimal> getType() {
        return BigDecimal.class;
    }
}
