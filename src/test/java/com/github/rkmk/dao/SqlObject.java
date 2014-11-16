package com.github.rkmk.dao;

import com.github.rkmk.container.FoldingList;
import com.github.rkmk.model.Movie;
import com.github.rkmk.model.Musician;

public interface SqlObject {

    public FoldingList<Movie> getMovie(Integer movieId);

    public FoldingList<Movie> getAllMovies();

    public FoldingList<Musician> getMusician();

}

