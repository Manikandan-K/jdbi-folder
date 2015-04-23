package com.github.rkmk.mapper;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import static java.util.Objects.nonNull;

public class EnumMapper implements ResultSetMapper<Enum> {

    private Class<? extends Enum> type;

    public EnumMapper(Class<? extends Enum> type) {
        this.type = type;
    }

    @Override
    public Enum map(int index, ResultSet r, StatementContext ctx) throws SQLException {
        String value = r.getString(1);
        return nonNull(value) ? Enum.valueOf(type, value) : null;
    }
}
