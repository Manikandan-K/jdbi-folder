package in.folder.jdbi.dao;

import in.folder.jdbi.MovieExceptionMapper;
import in.folder.jdbi.container.FoldingList;
import in.folder.jdbi.model.*;
import in.folder.jdbi.util.QueryLocator;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;
import org.skife.jdbi.v2.sqlobject.customizers.OverrideStatementLocatorWith;

import java.util.List;

@OverrideStatementLocatorWith(QueryLocator.class)
public interface SqlObjectDao {

    @SqlQuery
    @Mapper(MovieExceptionMapper.class)
    public List<Movie> getMovies();

    @SqlQuery
    List<Song> getSongs();

    @SqlQuery
    List<Team> getTeam();

    @SqlQuery
    public FoldingList<Movie> getMovie(@Bind("movieId") Integer movieId);

    @SqlQuery
    public FoldingList<Movie> getAllMovies();

    @SqlQuery
    public FoldingList<Musician> getMusician();


}
