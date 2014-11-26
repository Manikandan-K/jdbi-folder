package com.github.rkmk;

import com.github.rkmk.container.FoldingListContainerFactory;
import com.github.rkmk.mapper.CustomMapperFactory;
import com.github.rkmk.model.*;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;

public class DaoTest {
    protected static DBI dbi= getDBI();

    protected DBI getDbi() {
        return getDBI();
    }

    private static DBI getDBI() {
        return System.getProperty("db").equals("postgres") ? new DBI("jdbc:postgresql://localhost:5432/jdbi", "postgres", "") : new DBI("jdbc:mysql://localhost/jdbi", "root", "");
    }

    static {
        dbi.registerMapper(new CustomMapperFactory());
        dbi.registerContainerFactory(new FoldingListContainerFactory());
    }
    protected static Handle handle = dbi.open();

    @BeforeClass
    public static void createDefaultTables() {
        handle.execute("create table movie (movie_id numeric not null, movie_name varchar(100) not null, ratings numeric);");
        handle.execute("create table song ( movie_id numeric, song_id numeric not null, song_name varchar(100) not null, album_id numeric);");
        handle.execute("create table actor( movie_id numeric, actor_id numeric not null, actor_name varchar(100) not null);");
        handle.execute("create table director( movie_id numeric not null, director_id numeric not null, director_name varchar(100) not null);");
        handle.execute("create table musician (id numeric not null, name varchar(100) not null);");
        handle.execute("create table album (id numeric not null, name varchar(100) not null, musician_id numeric);");
        handle.execute("create table team (id numeric not null, name varchar(100) not null, average numeric);");
        handle.execute("create table primitive(intField integer, floatField numeric, doubleField numeric, booleanField boolean, longField numeric,intObjectField integer, floatObjectField numeric, doubleObjectField numeric, booleanObjectField boolean, longObjectField numeric );");
        handle.execute("create table assistant_director (id numeric not null, name varchar(100) not null, director_id numeric);");
    }


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
        handle.execute("delete from assistant_director");
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

    @AfterClass
    public static void tearDown() throws Exception {
        handle.execute("drop table movie");
        handle.execute("drop table song");
        handle.execute("drop table actor");
        handle.execute("drop table director");
        handle.execute("drop table musician");
        handle.execute("drop table album");
        handle.execute("drop table team");
        handle.execute("drop table primitive");
        handle.execute("drop table assistant_director");
    }

}
