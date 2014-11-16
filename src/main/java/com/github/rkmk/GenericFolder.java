package com.github.rkmk;

import com.github.rkmk.mapper.CustomMapper;
import com.github.rkmk.mapper.CustomMapperFactory;
import com.github.rkmk.mapper.FieldMapperFactory;
import org.skife.jdbi.v2.Folder2;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GenericFolder<T> implements Folder2<List<T>> {

    private final ResultSetMapper<T> mapper;
    private final List<T> acc;
    private final Folder folder;
    private static List<FieldMapperFactory> overriddenFactories  = new ArrayList<>();

    public GenericFolder(Class<T> type) {
        folder = new Folder();
        mapper = new CustomMapper<>(type, overriddenFactories);
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

    public static void register(FieldMapperFactory fieldMapperFactory) {
        overriddenFactories.add(fieldMapperFactory);
    }

}
