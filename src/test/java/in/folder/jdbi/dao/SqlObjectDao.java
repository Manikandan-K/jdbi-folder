package in.folder.jdbi.dao;

import in.folder.jdbi.MovieMapper;
import in.folder.jdbi.model.Movie;
import in.folder.jdbi.model.Song;
import in.folder.jdbi.model.Team;
import in.folder.jdbi.util.QueryLocator;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.OverrideStatementLocatorWith;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import java.util.List;

@OverrideStatementLocatorWith(QueryLocator.class)
public interface SqlObjectDao {

    @SqlQuery
    @RegisterMapper(MovieMapper.class)
    public List<Movie> getMovies();

    @SqlQuery
    List<Song> getSongs();

    @SqlQuery
    List<Team> getTeam();

}
