package com.github.rkmk.mapper;

import lombok.Getter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.postgresql.jdbc4.Jdbc4Array;
import org.skife.jdbi.v2.StatementContext;

import java.math.BigDecimal;
import java.sql.Array;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ArrayFieldMapperFactoryTest {

    @Mock
    ResultSet resultSet;
    @Mock
    ResultSetMetaData resultSetMetaData;
    @Mock
    StatementContext ctx;

    CustomMapper<SampleArrayBean> mapper = new CustomMapper<>(SampleArrayBean.class, new ArrayList<>());


    @Test
    public void shouldMapIntegerArrayValue() throws Exception {
        int[] intArray = {1, 2, 3};
        Integer[] integerArray = {1, 2, 3};
        Array intJdbcArray = buildJdbc4Array(intArray);
        Array integerJdbcArray = buildJdbc4Array(integerArray);

        when(resultSetMetaData.getColumnCount()).thenReturn(2);
        when(resultSetMetaData.getColumnLabel(1)).thenReturn("intArray");
        when(resultSetMetaData.getColumnLabel(2)).thenReturn("integerArray");
        when(resultSet.getMetaData()).thenReturn(resultSetMetaData);
        when(resultSet.getArray(1)).thenReturn(intJdbcArray);
        when(resultSet.getArray(2)).thenReturn(integerJdbcArray);

        SampleArrayBean sampleArrayBean = mapper.map(0, resultSet, ctx);

        assertSame(intArray, sampleArrayBean.getIntArray());
        assertSame(integerArray, sampleArrayBean.getIntegerArray());
    }

    @Test
    public void shouldReturnNullWhenNoValueIsPresent() throws Exception {
        when(resultSetMetaData.getColumnCount()).thenReturn(1);
        when(resultSetMetaData.getColumnLabel(1)).thenReturn("intArray");
        when(resultSet.getMetaData()).thenReturn(resultSetMetaData);
        doReturn(null).when(resultSet).getArray(1);
        doReturn(true).when(resultSet).wasNull();

        SampleArrayBean sampleArrayBean = mapper.map(0, resultSet, ctx);

        assertNull(sampleArrayBean.getIntArray());
    }

    @Test
    public void shouldMapArrayValues() throws Exception {
        Long[] longObjectArray = {1l, 2l, 3l};
        long[] longArray = {2, 4, 3};
        Float[] floatObjectArray = {2.2f, 4.1f, 3.6f};
        float[] floatArray = {2, 6, 3};
        Double[] doubleObjectArray = {2.3d, 4.1d, 6.6d};
        double[] doubleArray = {8, 6, 3};
        String[] stringArray = {"Hi", "Hello"};
        BigDecimal[] bigDecimalArray = {BigDecimal.ONE, BigDecimal.TEN, BigDecimal.ZERO};

        when(resultSet.getMetaData()).thenReturn(resultSetMetaData);
        when(resultSetMetaData.getColumnCount()).thenReturn(8);

        when(resultSetMetaData.getColumnLabel(1)).thenReturn("longObjectArray");
        when(resultSetMetaData.getColumnLabel(2)).thenReturn("longArray");
        when(resultSetMetaData.getColumnLabel(3)).thenReturn("floatObjectArray");
        when(resultSetMetaData.getColumnLabel(4)).thenReturn("floatArray");
        when(resultSetMetaData.getColumnLabel(5)).thenReturn("doubleObjectArray");
        when(resultSetMetaData.getColumnLabel(6)).thenReturn("doubleArray");
        when(resultSetMetaData.getColumnLabel(7)).thenReturn("stringArray");
        when(resultSetMetaData.getColumnLabel(8)).thenReturn("bigDecimalArray");

        doReturn(buildJdbc4Array(longObjectArray)).when(resultSet).getArray(1);
        doReturn(buildJdbc4Array(longArray)).when(resultSet).getArray(2);
        doReturn(buildJdbc4Array(floatObjectArray)).when(resultSet).getArray(3);
        doReturn(buildJdbc4Array(floatArray)).when(resultSet).getArray(4);
        doReturn(buildJdbc4Array(doubleObjectArray)).when(resultSet).getArray(5);
        doReturn(buildJdbc4Array(doubleArray)).when(resultSet).getArray(6);
        doReturn(buildJdbc4Array(stringArray)).when(resultSet).getArray(7);
        doReturn(buildJdbc4Array(bigDecimalArray)).when(resultSet).getArray(8);

        SampleArrayBean sampleArrayBean = mapper.map(0, resultSet, ctx);

        assertSame(longObjectArray, sampleArrayBean.getLongObjectArray());
        assertSame(longArray, sampleArrayBean.getLongArray());
        assertSame(floatObjectArray, sampleArrayBean.getFloatObjectArray());
        assertSame(floatArray, sampleArrayBean.getFloatArray());
        assertSame(doubleObjectArray, sampleArrayBean.getDoubleObjectArray());
        assertSame(doubleArray, sampleArrayBean.getDoubleArray());
        assertSame(stringArray, sampleArrayBean.getStringArray());
        assertSame(bigDecimalArray, sampleArrayBean.getBigDecimalArray());
    }

    public <T> Jdbc4Array buildJdbc4Array(T array) throws SQLException {
        Jdbc4Array jdbc4Array = mock(Jdbc4Array.class);
        when(jdbc4Array.getArray()).thenReturn(array);
        return jdbc4Array;
    }

}

@Getter
class SampleArrayBean {
    private Long[] longObjectArray;
    private long[] longArray;
    private Float[] floatObjectArray;
    private float[] floatArray;
    private Double[] doubleObjectArray;
    private double[] doubleArray;
    private String[] stringArray;
    private int[] intArray;
    private Integer[] integerArray;
    private BigDecimal[] bigDecimalArray;
}

