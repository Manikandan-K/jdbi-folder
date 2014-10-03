package in.folder.jdbi.mapper;

import org.skife.jdbi.v2.ResultSetMapperFactory;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.util.ArrayList;
import java.util.List;

public class CustomMapperFactory implements ResultSetMapperFactory {

    private List<Class<?>> excludedTypes = new ArrayList<>();

    public CustomMapperFactory() {
    }

    public CustomMapperFactory(Class<?>... excludedTypes) {
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
        return new CustomMapper<>(type);
    }
}
