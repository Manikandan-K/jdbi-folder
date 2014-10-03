package in.folder.jdbi.mapper;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class FieldMapperFactories {

    private HashMap<Class<?>, FieldMapperFactory> factories =new HashMap<>();

    public FieldMapperFactories() {
        this.factories.put(Boolean.class, new BooleanMapperFactory());
        this.factories.put(BigDecimal.class, new BigDecimalMapperFactory());
    }

    public HashMap<Class<?>, FieldMapperFactory> getValues() {
        return factories;
    }


    public static class BooleanMapperFactory implements FieldMapperFactory<Boolean>{
        @Override
        public Boolean getValue(ResultSet rs, int index) throws SQLException {
            return rs.getBoolean(index);
        }

        @Override
        public Class<Boolean> getType() {
            return Boolean.class;
        }
    }

    public static class ByteMapperFactory implements FieldMapperFactory<Byte>{
        @Override
        public Byte getValue(ResultSet rs, int index) throws SQLException {
            return rs.getByte(index);
        }

        @Override
        public Class<Byte> getType() {
            return Byte.class;
        }
    }

    public static class BigDecimalMapperFactory implements FieldMapperFactory<BigDecimal>{
        @Override
        public BigDecimal getValue(ResultSet rs, int index) throws SQLException {
            return rs.getBigDecimal(index);
        }

        @Override
        public Class<BigDecimal> getType() {
            return BigDecimal.class;
        }
    }

}
