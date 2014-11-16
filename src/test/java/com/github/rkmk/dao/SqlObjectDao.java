package com.github.rkmk.dao;

import com.github.rkmk.MovieExceptionMapper;
import com.github.rkmk.container.FoldingList;
import com.github.rkmk.util.QueryLocator;
import com.github.rkmk.model.*;
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
