package com.github.rkmk.model;

import com.github.rkmk.mapper.CustomMapper;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MovieMapper implements ResultSetMapper<Movie> {

    private final CustomMapper<Movie> mapper;

    public MovieMapper() {
        this.mapper = new CustomMapper<>(Movie.class);
    }

    @Override
    public Movie map(int index, ResultSet r, StatementContext ctx) throws SQLException {
        return mapper.map(index, r, ctx);
    }
}
