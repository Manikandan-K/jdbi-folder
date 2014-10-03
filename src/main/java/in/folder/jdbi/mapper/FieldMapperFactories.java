package in.folder.jdbi.mapper;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FieldMapperFactories {

    private List<FieldMapperFactory> factories = new ArrayList<>();

    public FieldMapperFactories() {
        factories.add(new BooleanMapperFactory());
        factories.add(new ByteMapperFactory());
        factories.add(new ShortMapperFactory());
        factories.add(new IntegerMapperFactory());
        factories.add(new LongMapperFactory());
        factories.add(new FloatMapperFactory());
        factories.add(new DoubleMapperFactory());
        factories.add(new BigDecimalMapperFactory());
        factories.add(new TimestampMapperFactory());
        factories.add(new TimeMapperFactory());
        factories.add(new DateMapperFactory());
        factories.add(new StringMapperFactory());
    }

    public List<FieldMapperFactory> getValues() {
        return factories;
    }


    public static class BooleanMapperFactory implements FieldMapperFactory<Boolean>{
        @Override
        public Boolean getValue(ResultSet rs, int index) throws SQLException {
            return rs.getBoolean(index);
        }

        @Override
        public Boolean accepts(Class<?> type) {
            return type.isAssignableFrom(Boolean.class) || type.isAssignableFrom(boolean.class);
        }
    }

    public static class ByteMapperFactory implements FieldMapperFactory<Byte>{
        @Override
        public Byte getValue(ResultSet rs, int index) throws SQLException {
            return rs.getByte(index);
        }

        @Override
        public Boolean accepts(Class<?> type) {
            return type.isAssignableFrom(Byte.class) || type.isAssignableFrom(byte.class);
        }
    }

    public static class ShortMapperFactory implements FieldMapperFactory<Short>{
        @Override
        public Short getValue(ResultSet rs, int index) throws SQLException {
            return rs.getShort(index);
        }

        @Override
        public Boolean accepts(Class<?> type) {
            return type.isAssignableFrom(Short.class) || type.isAssignableFrom(short.class);
        }
    }

    public static class IntegerMapperFactory implements FieldMapperFactory<Integer>{
        @Override
        public Integer getValue(ResultSet rs, int index) throws SQLException {
            return rs.getInt(index);
        }

        @Override
        public Boolean accepts(Class<?> type) {
            return type.isAssignableFrom(Integer.class) || type.isAssignableFrom(int.class);
        }
    }

    public static class LongMapperFactory implements FieldMapperFactory<Long>{
        @Override
        public Long getValue(ResultSet rs, int index) throws SQLException {
            return rs.getLong(index);
        }

        @Override
        public Boolean accepts(Class<?> type) {
            return type.isAssignableFrom(Long.class) || type.isAssignableFrom(long.class);
        }
    }

    public static class FloatMapperFactory implements FieldMapperFactory<Float>{
        @Override
        public Float getValue(ResultSet rs, int index) throws SQLException {
            return rs.getFloat(index);
        }

        @Override
        public Boolean accepts(Class<?> type) {
            return type.isAssignableFrom(Float.class) || type.isAssignableFrom(float.class);
        }
    }

    public static class DoubleMapperFactory implements FieldMapperFactory<Double>{
        @Override
        public Double getValue(ResultSet rs, int index) throws SQLException {
            return rs.getDouble(index);
        }

        @Override
        public Boolean accepts(Class<?> type) {
            return type.isAssignableFrom(Double.class) || type.isAssignableFrom(double.class);
        }
    }

    public static class BigDecimalMapperFactory implements FieldMapperFactory<BigDecimal>{
        @Override
        public BigDecimal getValue(ResultSet rs, int index) throws SQLException {
            return rs.getBigDecimal(index);
        }

        @Override
        public Boolean accepts(Class<?> type) {
            return type.isAssignableFrom(BigDecimal.class);
        }
    }

    public static class TimestampMapperFactory implements FieldMapperFactory<Timestamp>{
        @Override
        public Timestamp getValue(ResultSet rs, int index) throws SQLException {
            return rs.getTimestamp(index);
        }

        @Override
        public Boolean accepts(Class<?> type) {
            return type.isAssignableFrom(Timestamp.class);
        }
    }

    public static class TimeMapperFactory implements FieldMapperFactory<Time>{
        @Override
        public Time getValue(ResultSet rs, int index) throws SQLException {
            return rs.getTime(index);
        }

        @Override
        public Boolean accepts(Class<?> type) {
            return type.isAssignableFrom(Time.class);
        }
    }

    public static class DateMapperFactory implements FieldMapperFactory<Date>{
        @Override
        public Date getValue(ResultSet rs, int index) throws SQLException {
            return rs.getDate(index);
        }

        @Override
        public Boolean accepts(Class<?> type) {
            return type.isAssignableFrom(Date.class);
        }
    }

    public static class StringMapperFactory implements FieldMapperFactory<String>{
        @Override
        public String getValue(ResultSet rs, int index) throws SQLException {
            return rs.getString(index);
        }

        @Override
        public Boolean accepts(Class<?> type) {
            return type.isAssignableFrom(String.class);
        }
    }

}
