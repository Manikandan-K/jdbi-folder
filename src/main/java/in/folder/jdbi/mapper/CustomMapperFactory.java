package in.folder.jdbi.mapper;

import org.skife.jdbi.v2.ResultSetMapperFactory;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CustomMapperFactory implements ResultSetMapperFactory {

    private List<Class<?>> excludedTypes = new ArrayList<>();
    private List<FieldMapperFactory> overriddenFactories = new ArrayList<>();

    public CustomMapperFactory() {
        excludedTypes.add(Boolean.class);
        excludedTypes.add(Byte.class);
        excludedTypes.add(Short.class);
        excludedTypes.add(Integer.class);
        excludedTypes.add(Long.class);
        excludedTypes.add(Float.class);
        excludedTypes.add(Double.class);
        excludedTypes.add(BigDecimal.class);
        excludedTypes.add(String.class);
    }

    public CustomMapperFactory(Class<?>... excludedTypes) {
        this();
        for (Class<?> excludedType : excludedTypes) {
            this.excludedTypes.add(excludedType);
        }
    }

    @Override
    public boolean accepts(Class type, StatementContext ctx) {
        return !excludedTypes.contains(type);
    }

    @Override
    public ResultSetMapper mapperFor(Class type, StatementContext ctx) {
        return new CustomMapper<>(type, overriddenFactories);
    }

    public void register(FieldMapperFactory factory) {
        overriddenFactories.add(factory);
    }
}
