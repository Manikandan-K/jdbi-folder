package in.folder.jdbi;

import in.folder.jdbi.model.Movie;
import in.folder.jdbi.model.Song;
import org.junit.Before;
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;

public class DaoTest {
    protected static DBI dbi= new DBI("jdbc:postgresql://localhost:5432/jdbi","postgres","pass");
    protected static Handle handle = dbi.open();

    @Before
    public void clear() {
        handle.execute("delete from movie");
        handle.execute("delete from song");
    }

    protected void insert(Movie... movies) {
        for (Movie movie : movies) {
            handle.execute("insert into movie(movie_id, movie_name) values(?,?)", movie.getMovieId(), movie.getMovieName());
        }
    }

    protected void insert(Song... songs) {
        for (Song song : songs) {
            handle.execute("insert into song(movie_id, song_id, song_name) values(?,?,?)", song.getMovieId(), song.getSongId(), song.getSongName());
        }
    }

}
