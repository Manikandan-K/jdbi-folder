package in.folder.jdbi.mapper;

import org.skife.jdbi.v2.ResultSetMapperFactory;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CustomMapperFactory implements ResultSetMapperFactory {

    private List<Class<?>> excludedTypes = new ArrayList<>();
    private HashMap<Class<?>, FieldMapperFactory> factories = new HashMap<>();

    public CustomMapperFactory() {
        factories.putAll(new FieldMapperFactories().getValues());
    }

    public CustomMapperFactory(Class<?>... excludedTypes) {
        for (Class<?> excludedType : excludedTypes) {
            this.excludedTypes.add(excludedType);
        }
        factories.putAll(new FieldMapperFactories().getValues());
    }

    @Override
    public boolean accepts(Class type, StatementContext ctx) {
        return !excludedTypes.contains(type);
    }

    @Override
    public ResultSetMapper mapperFor(Class type, StatementContext ctx) {
        return new CustomMapper<>(type, factories);
    }

    public void register(FieldMapperFactory factory) {
        factories.put(factory.getType(), factory);
    }
}
