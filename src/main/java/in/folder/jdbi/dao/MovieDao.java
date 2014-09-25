package in.folder.jdbi.dao;

import in.folder.jdbi.GenericFolder;
import in.folder.jdbi.model.Movie;
import in.folder.jdbi.util.QueryLocator;
import org.skife.jdbi.v2.sqlobject.mixins.GetHandle;

import java.util.List;

public abstract class MovieDao implements GetHandle {

    private QueryLocator locator = new QueryLocator();

    public Movie getMovie(Integer movieId) throws Exception {
        String query = locator.locate("getMovie");
        GenericFolder<Movie> folder = new GenericFolder<>(Movie.class);

        List<Movie> movies = getHandle().createQuery(query)
                .bind("movieId", movieId)
                .fold(folder.getAccumulator(), folder);

        return movies.size() > 0 ? movies.get(0) : null;
    }
}
