package in.folder.jdbi;

import in.folder.jdbi.model.Actor;
import in.folder.jdbi.model.Director;
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
        handle.execute("delete from actor");
        handle.execute("delete from director");
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

    protected void insert(Actor... actors) {
        for (Actor actor : actors) {
            handle.execute("insert into actor(movie_id, actor_id, actor_name) values(?,?,?)", actor.getMovieId(), actor.getActorId(), actor.getActorName());
        }
    }

    protected void insert(Director... directors) {
        for (Director director : directors) {
            handle.execute("insert into director(movie_id, director_id, director_name) values(?,?,?)", director.getMovieId(), director.getDirectorId(), director.getDirectorName());
        }
    }

}
