package in.folder.jdbi.model;

import in.folder.jdbi.mapper.CustomMapper1;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MovieMapper implements ResultSetMapper<Movie> {

    private final CustomMapper1<Movie> mapper;

    public MovieMapper() {
        this.mapper = new CustomMapper1<>(Movie.class);
    }

    @Override
    public Movie map(int index, ResultSet r, StatementContext ctx) throws SQLException {
        return mapper.map(index, r, ctx);
    }
}
