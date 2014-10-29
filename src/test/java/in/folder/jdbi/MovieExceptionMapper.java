package in.folder.jdbi;

import in.folder.jdbi.model.Movie;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MovieExceptionMapper implements ResultSetMapper<Movie> {
    @Override
    public Movie map(int index, ResultSet r, StatementContext ctx) throws SQLException {
        throw new RuntimeException("It came to Movie mapper");
    }
}
