package com.github.rkmk.mapper;

import org.junit.Test;

import java.sql.Array;
import java.sql.ResultSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CollectionFieldMapperFactoryTest {

    @Test
    public void shouldConvertTheSqlArrayToList() throws Exception {
        FieldMapperFactories.ListMapperFactory listMapperFactory = new FieldMapperFactories.ListMapperFactory();
        Array array = mock(Array.class);
        when(array.getArray()).thenReturn(new Integer[] {1,2,3,4});
        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.getArray(1)).thenReturn(array);

        List<Integer> result = listMapperFactory.getValue(resultSet, 1, null);

        assertEquals(4, result.size());
    }

    @Test
    public void shouldHandleNullWhileConvertingTheSqlArrayToList() throws Exception {
        FieldMapperFactories.ListMapperFactory listMapperFactory = new FieldMapperFactories.ListMapperFactory();
        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.getArray(1)).thenReturn(null);

        List<Integer> result = listMapperFactory.getValue(resultSet, 1, null);

        assertEquals(0, result.size());
    }

    @Test
    public void shouldConvertThsSqlArrayToSet() throws Exception {
        FieldMapperFactories.SetMapperFactory setMapperFactory = new FieldMapperFactories.SetMapperFactory();
        Array array = mock(Array.class);
        when(array.getArray()).thenReturn(new String[] {"A for Apple","B for Bat"});
        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.getArray(1)).thenReturn(array);

        Set<String> result = setMapperFactory.getValue(resultSet, 1, null);

        assertEquals(2, result.size());
    }

    @Test
    public void shouldHandleNullWhileConvertingTheSqlArrayToSet() throws Exception {
        FieldMapperFactories.SetMapperFactory setMapperFactory = new FieldMapperFactories.SetMapperFactory();
        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.getArray(1)).thenReturn(null);

        Set<Integer> result = setMapperFactory.getValue(resultSet, 1, null);

        assertEquals(0, result.size());
    }


}
