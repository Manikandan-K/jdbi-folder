package in.folder.jdbi;

import in.folder.jdbi.model.*;
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
        handle.execute("delete from musician");
        handle.execute("delete from album");
    }

    protected void insert(Movie... movies) {
        for (Movie movie : movies) {
            handle.execute("insert into movie(movie_id, movie_name) values(?,?)", movie.getMovieId(), movie.getMovieName());
        }
    }

    protected void insert(Song... songs) {
        for (Song song : songs) {
            handle.execute("insert into song(movie_id, song_id, song_name, album_id) values(?,?,?,?)", song.getMovieId(), song.getSongId(), song.getSongName(), song.getAlbumId());
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

    protected void insert(Musician... musicians) {
        for (Musician musician : musicians) {
            handle.execute("insert into musician(id, name) values(?,?)", musician.getId(), musician.getName());
        }
    }

    protected void insert(Album... albums) {
        for (Album album : albums) {
            handle.execute("insert into album(id, name, musician_id) values(?,?,?)", album.getId(), album.getName(), album.getMusicianId());
        }
    }

}
