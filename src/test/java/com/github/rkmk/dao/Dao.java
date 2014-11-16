package com.github.rkmk.dao;

import com.github.rkmk.GenericFolder;
import com.github.rkmk.model.Movie;
import com.github.rkmk.model.Musician;
import com.github.rkmk.util.QueryLocator;
import org.skife.jdbi.v2.sqlobject.mixins.GetHandle;

import java.util.List;

public abstract class Dao implements GetHandle {

    private QueryLocator locator = new QueryLocator();

    public Movie getMovie(Integer movieId) throws Exception {
        String query = locator.locate("getMovie");
        GenericFolder<Movie> folder = new GenericFolder<>(Movie.class);

        List<Movie> movies = getHandle().createQuery(query)
                .bind("movieId", movieId)
                .fold(folder.getAccumulator(), folder);

        return movies.size() > 0 ? movies.get(0) : null;
    }

    public List<Movie> getAllMovies() throws Exception {
        String query = locator.locate("getAllMovies");
        GenericFolder<Movie> folder = new GenericFolder<>(Movie.class);

        return getHandle().createQuery(query)
                .fold(folder.getAccumulator(), folder);
    }

    public List<Musician> getMusician()  throws Exception {
        String query = locator.locate("getMusician");
        GenericFolder<Musician> folder = new GenericFolder<>(Musician.class);

        return getHandle().createQuery(query)
                .fold(folder.getAccumulator(), folder);
    }
    public List<Movie> getMovieDirector()  throws Exception {
        String query = locator.locate("getMovieDirector");
        GenericFolder<Movie> folder = new GenericFolder<>(Movie.class);

        return getHandle().createQuery(query)
                .fold(folder.getAccumulator(), folder);
    }

}

