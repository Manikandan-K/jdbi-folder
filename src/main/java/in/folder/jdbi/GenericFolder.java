package in.folder.jdbi;

import in.folder.jdbi.mapper.CustomMapperFactory;
import in.folder.jdbi.mapper.FieldMapperFactory;
import org.skife.jdbi.v2.Folder2;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GenericFolder<T> implements Folder2<List<T>> {

    private ResultSetMapper<T> mapper;
    private List<T> acc;
    private static List<FieldMapperFactory> overriddenFactories = new ArrayList<>();

    private final Folder folder;

    public GenericFolder(Class<T> type) {
        folder = new Folder();
        mapper = CustomMapperFactory.mapperFor(type, "");
        acc = new ArrayList<>();
    }

    public List<T> getAccumulator(){
        return acc;
    }

    @Override
    public List<T> fold(List<T> accumulator, ResultSet rs, StatementContext ctx) throws SQLException {
        T object = mapper.map(rs.getRow(), rs, ctx);
        folder.fold(accumulator, object);
        return accumulator;
    }

    public static void register(FieldMapperFactory factory) {
        overriddenFactories.add(factory);
    }

    static List<FieldMapperFactory> getOverriddenFactories(){
        return overriddenFactories;
    }

}
