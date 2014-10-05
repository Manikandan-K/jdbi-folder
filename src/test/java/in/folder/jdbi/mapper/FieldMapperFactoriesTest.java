package in.folder.jdbi.mapper;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertTrue;

public class FieldMapperFactoriesTest {

    @Test
    public void objectMapperFactoryShouldBeLastFactory() throws Exception {
        FieldMapperFactories fieldMapperFactories = new FieldMapperFactories();

        List<FieldMapperFactory> factories = fieldMapperFactories.getValues();

        assertTrue( factories.get(factories.size() - 1) instanceof FieldMapperFactories.ObjectMapperFactory);
        assertTrue( factories.get(factories.size() - 2) instanceof FieldMapperFactories.EnumMapperFactory);
    }
}
