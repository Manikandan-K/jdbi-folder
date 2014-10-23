package in.folder.jdbi.dao;

import in.folder.jdbi.container.FoldingList;
import in.folder.jdbi.model.Movie;
import in.folder.jdbi.model.Musician;

public interface SqlObject {

    public FoldingList<Movie> getMovie(Integer movieId);

    public FoldingList<Movie> getAllMovies();

    public FoldingList<Musician> getMusician();

}

