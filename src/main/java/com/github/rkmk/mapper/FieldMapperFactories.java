package com.github.rkmk.mapper;

import java.math.BigDecimal;
import java.sql.*;
import java.util.*;
import java.util.Date;

import static java.util.Arrays.asList;
import static java.util.Objects.nonNull;

public class FieldMapperFactories {

    private List<FieldMapperFactory> factories = new ArrayList<>();

    public FieldMapperFactories() {
        factories.add(new BooleanMapperFactory());
        factories.add(new ByteMapperFactory());
        factories.add(new ShortMapperFactory());
        factories.add(new IntegerMapperFactory());
        factories.add(new IntegerArrayMapperFactory());
        factories.add(new IntArrayMapperFactory());
        factories.add(new LongMapperFactory());
        factories.add(new LongObjectArrayMapperFactory());
        factories.add(new LongArrayMapperFactory());
        factories.add(new FloatMapperFactory());
        factories.add(new FloatObjectArrayMapperFactory());
        factories.add(new FloatArrayMapperFactory());
        factories.add(new DoubleMapperFactory());
        factories.add(new DoubleObjectArrayMapperFactory());
        factories.add(new DoubleArrayMapperFactory());
        factories.add(new BigDecimalMapperFactory());
        factories.add(new BigDecimalArrayMapperFactory());
        factories.add(new TimestampMapperFactory());
        factories.add(new TimeMapperFactory());
        factories.add(new DateMapperFactory());
        factories.add(new StringMapperFactory());
        factories.add(new StringArrayMapperFactory());
        factories.add(new ListMapperFactory());
        factories.add(new SetMapperFactory());
        factories.add(new EnumMapperFactory());
        factories.add(new ObjectMapperFactory());
    }

    public List<FieldMapperFactory> getValues() {
        return factories;
    }


    public static class BooleanMapperFactory implements FieldMapperFactory<Boolean>{
        @Override
        public Boolean getValue(ResultSet rs, int index, Class<?> type) throws SQLException {
            return getNullOrValue(type,rs, rs.getBoolean(index));
        }

        @Override
        public Boolean accepts(Class<?> type) {
            return type.isAssignableFrom(Boolean.class) || type.isAssignableFrom(boolean.class);
        }
    }

    public static class ByteMapperFactory implements FieldMapperFactory<Byte>{
        @Override
        public Byte getValue(ResultSet rs, int index, Class<?> type) throws SQLException {
            return getNullOrValue(type,rs, rs.getByte(index));
        }

        @Override
        public Boolean accepts(Class<?> type) {
            return type.isAssignableFrom(Byte.class) || type.isAssignableFrom(byte.class);
        }
    }

    public static class ShortMapperFactory implements FieldMapperFactory<Short>{
        @Override
        public Short getValue(ResultSet rs, int index, Class<?> type) throws SQLException {
            return getNullOrValue(type,rs, rs.getShort(index));
        }

        @Override
        public Boolean accepts(Class<?> type) {
            return type.isAssignableFrom(Short.class) || type.isAssignableFrom(short.class);
        }
    }

    public static class IntegerMapperFactory implements FieldMapperFactory<Integer>{
        @Override
        public Integer getValue(ResultSet rs, int index, Class<?> type) throws SQLException {
            return getNullOrValue(type,rs, rs.getInt(index));
        }

        @Override
        public Boolean accepts(Class<?> type) {
            return type.isAssignableFrom(Integer.class) || type.isAssignableFrom(int.class);
        }
    }

    public static class LongMapperFactory implements FieldMapperFactory<Long>{
        @Override
        public Long getValue(ResultSet rs, int index, Class<?> type) throws SQLException {
            return getNullOrValue(type,rs, rs.getLong(index));
        }

        @Override
        public Boolean accepts(Class<?> type) {
            return type.isAssignableFrom(Long.class) || type.isAssignableFrom(long.class);
        }
    }

    public static class FloatMapperFactory implements FieldMapperFactory<Float>{
        @Override
        public Float getValue(ResultSet rs, int index, Class<?> type) throws SQLException {
            return getNullOrValue(type,rs, rs.getFloat(index));
        }

        @Override
        public Boolean accepts(Class<?> type) {
            return type.isAssignableFrom(Float.class) || type.isAssignableFrom(float.class);
        }
    }

    public static class DoubleMapperFactory implements FieldMapperFactory<Double>{
        @Override
        public Double getValue(ResultSet rs, int index, Class<?> type) throws SQLException {
            return getNullOrValue(type,rs, rs.getDouble(index));
        }

        @Override
        public Boolean accepts(Class<?> type) {
            return type.isAssignableFrom(Double.class) || type.isAssignableFrom(double.class);
        }
    }

    public static class BigDecimalMapperFactory implements FieldMapperFactory<BigDecimal>{
        @Override
        public BigDecimal getValue(ResultSet rs, int index, Class<?> type) throws SQLException {
            return getNullOrValue(type,rs, rs.getBigDecimal(index));
        }

        @Override
        public Boolean accepts(Class<?> type) {
            return type.isAssignableFrom(BigDecimal.class);
        }
    }

    public static class TimestampMapperFactory implements FieldMapperFactory<Timestamp>{
        @Override
        public Timestamp getValue(ResultSet rs, int index, Class<?> type) throws SQLException {
            return getNullOrValue(type,rs, rs.getTimestamp(index));
        }

        @Override
        public Boolean accepts(Class<?> type) {
            return type.isAssignableFrom(Timestamp.class);
        }
    }

    public static class TimeMapperFactory implements FieldMapperFactory<Time>{
        @Override
        public Time getValue(ResultSet rs, int index, Class<?> type) throws SQLException {
            return getNullOrValue(type,rs, rs.getTime(index));
        }

        @Override
        public Boolean accepts(Class<?> type) {
            return type.isAssignableFrom(Time.class);
        }
    }

    public static class DateMapperFactory implements FieldMapperFactory<Date>{
        @Override
        public Date getValue(ResultSet rs, int index, Class<?> type) throws SQLException {
            Date date = new Date(rs.getDate(index).getTime());
            return getNullOrValue(type,rs, date);
        }

        @Override
        public Boolean accepts(Class<?> type) {
            return type.isAssignableFrom(Date.class);
        }
    }

    public static class StringMapperFactory implements FieldMapperFactory<String>{
        @Override
        public String getValue(ResultSet rs, int index, Class<?> type) throws SQLException {
            return getNullOrValue(type,rs, rs.getString(index));
        }

        @Override
        public Boolean accepts(Class<?> type) {
            return type.isAssignableFrom(String.class);
        }
    }

    public static class IntegerArrayMapperFactory implements FieldMapperFactory<Integer[]>{

        @Override
        public Integer[] getValue(ResultSet rs, int index, Class<?> type) throws SQLException {
            Array array = rs.getArray(index);
            return nonNull(array) ? (Integer[])array.getArray() : null ;
        }

        @Override
        public Boolean accepts(Class<?> type) {
            return type == Integer[].class;
        }
    }

    public static class IntArrayMapperFactory implements FieldMapperFactory<int[]>{

        @Override
        public int[] getValue(ResultSet rs, int index, Class<?> type) throws SQLException {
            Array array = rs.getArray(index);
            return nonNull(array) ? (int[])array.getArray() : null ;
        }

        @Override
        public Boolean accepts(Class<?> type) {
            return type == int[].class;
        }
    }

    public static class StringArrayMapperFactory implements FieldMapperFactory<String[]>{

        @Override
        public String[] getValue(ResultSet rs, int index, Class<?> type) throws SQLException {
            Array array = rs.getArray(index);
            return nonNull(array) ? (String[])array.getArray() : null ;
        }

        @Override
        public Boolean accepts(Class<?> type) {
            return type == String[].class;
        }
    }

    public static class LongObjectArrayMapperFactory implements FieldMapperFactory<Long[]>{

        @Override
        public Long[] getValue(ResultSet rs, int index, Class<?> type) throws SQLException {
            Array array = rs.getArray(index);
            return nonNull(array) ? (Long[])array.getArray() : null ;
        }

        @Override
        public Boolean accepts(Class<?> type) {
            return type == Long[].class;
        }
    }

    public static class LongArrayMapperFactory implements FieldMapperFactory<long[]>{

        @Override
        public long[] getValue(ResultSet rs, int index, Class<?> type) throws SQLException {
            Array array = rs.getArray(index);
            return nonNull(array) ? (long[])array.getArray() : null ;
        }

        @Override
        public Boolean accepts(Class<?> type) {
            return type == long[].class;
        }
    }

    public static class FloatObjectArrayMapperFactory implements FieldMapperFactory<Float[]>{

        @Override
        public Float[] getValue(ResultSet rs, int index, Class<?> type) throws SQLException {
            Array array = rs.getArray(index);
            return nonNull(array) ? (Float[])array.getArray() : null ;
        }

        @Override
        public Boolean accepts(Class<?> type) {
            return type == Float[].class;
        }
    }

    public static class FloatArrayMapperFactory implements FieldMapperFactory<float[]>{

        @Override
        public float[] getValue(ResultSet rs, int index, Class<?> type) throws SQLException {
            Array array = rs.getArray(index);
            return nonNull(array) ? (float[])array.getArray() : null ;
        }

        @Override
        public Boolean accepts(Class<?> type) {
            return type == float[].class;
        }
    }

    public static class DoubleObjectArrayMapperFactory implements FieldMapperFactory<Double[]>{

        @Override
        public Double[] getValue(ResultSet rs, int index, Class<?> type) throws SQLException {
            Array array = rs.getArray(index);
            return nonNull(array) ? (Double[])array.getArray() : null ;
        }

        @Override
        public Boolean accepts(Class<?> type) {
            return type == Double[].class;
        }
    }

    public static class DoubleArrayMapperFactory implements FieldMapperFactory<double[]>{

        @Override
        public double[] getValue(ResultSet rs, int index, Class<?> type) throws SQLException {
            Array array = rs.getArray(index);
            return nonNull(array) ? (double[])array.getArray() : null ;
        }

        @Override
        public Boolean accepts(Class<?> type) {
            return type == double[].class ;
        }
    }

    public static class BigDecimalArrayMapperFactory implements FieldMapperFactory<BigDecimal[]>{

        @Override
        public BigDecimal[] getValue(ResultSet rs, int index, Class<?> type) throws SQLException {
            Array array = rs.getArray(index);
            return nonNull(array) ? (BigDecimal[])array.getArray() : null ;
        }

        @Override
        public Boolean accepts(Class<?> type) {
            return type == BigDecimal[].class;
        }
    }

    public static class EnumMapperFactory implements FieldMapperFactory<Enum> {

        @Override
        public Enum getValue(ResultSet rs, int index, Class<?> type) throws SQLException {
            String value = rs.getString(index);
            return nonNull(value) ? Enum.valueOf((Class<Enum>) type, value) : null ;
        }

        @Override
        public Boolean accepts(Class<?> type) {
            return type.isEnum();
        }
    }

    public static class ObjectMapperFactory implements FieldMapperFactory<Object> {

        @Override
        public Object getValue(ResultSet rs, int index, Class<?> type) throws SQLException {
            return getNullOrValue(type, rs, rs.getObject(index));
        }

        @Override
        public Boolean accepts(Class<?> type) {
            return true;
        }
    }

    public static class ListMapperFactory implements FieldMapperFactory<List> {

        @Override
        public List getValue(ResultSet rs, int index, Class<?> type) throws SQLException {
            Array sqlArray = rs.getArray(index);
            return (sqlArray == null) ? new ArrayList<>() : new ArrayList<>(asList((Object[]) sqlArray.getArray()));
        }

        @Override
        public Boolean accepts(Class<?> type) {
            return type.isAssignableFrom(List.class);
        }
    }

    public static class SetMapperFactory implements FieldMapperFactory<Set> {

        @Override
        public Set getValue(ResultSet rs, int index, Class<?> type) throws SQLException {
            Set<Object> result = new HashSet<>();
            Array sqlArray = rs.getArray(index);
            if(sqlArray == null) {
                return new HashSet<>();
            }

            Object[] array = (Object[]) sqlArray.getArray();
            for(Object element : array) {
                result.add(element);
            }
            return result;
        }

        @Override
        public Boolean accepts(Class<?> type) {
            return type.isAssignableFrom(Set.class);
        }
    }

}
