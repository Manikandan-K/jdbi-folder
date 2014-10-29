package in.folder.jdbi;

import in.folder.jdbi.container.FoldingListContainerFactory;
import in.folder.jdbi.mapper.BigDecimalMapperFactory;
import in.folder.jdbi.mapper.CustomMapperFactory;
import in.folder.jdbi.model.*;
import org.junit.Before;
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;

public class DaoTest {
    protected static DBI dbi= new DBI("jdbc:postgresql://localhost:5432/jdbi","postgres","pass");
    static {
        CustomMapperFactory factory = new CustomMapperFactory();
        factory.register(new BigDecimalMapperFactory());
        dbi.registerMapper(factory);
        dbi.registerContainerFactory(new FoldingListContainerFactory());
    }
    protected static Handle handle = dbi.open();

    @Before
    public void clear() {
        handle.execute("delete from movie");
        handle.execute("delete from song");
        handle.execute("delete from actor");
        handle.execute("delete from director");
        handle.execute("delete from musician");
        handle.execute("delete from album");
        handle.execute("delete from team");
        handle.execute("delete from primitive");
    }

    protected void insert(Movie... movies) {
        for (Movie movie : movies) {
            handle.execute("insert into movie(movie_id, movie_name, ratings) values(?,?,?)", movie.getMovieId(), movie.getMovieName(),movie.getRatings());
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

    protected void insert(Team... teams) {
        for (Team team : teams) {
            handle.execute("insert into team(id, name, average) values(?,?,?)", team.getTeamId(), team.getTeamName(), team.getAverage());
        }
    }

    protected void insert(AssistantDirector... assistantDirectors) {
        for (AssistantDirector assistantDirector : assistantDirectors) {
            handle.execute("insert into assistant_director(id, name, director_id) values(?,?,?)", assistantDirector.getId(), assistantDirector.getName(), assistantDirector.getDirectorId());
        }

    }




}
