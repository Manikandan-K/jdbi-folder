package com.github.rkmk.dao;

import com.github.rkmk.DaoTest;
import com.github.rkmk.model.Actor;
import com.github.rkmk.model.Movie;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;

import static org.junit.Assert.assertEquals;

public class MapSameObjectMultipleTimesTest extends DaoTest {

    private MovieDao dao;

    @Before
    public void before() {
        handle.execute("create table movie_actor( movie_id numeric not null, hero_id numeric not null, comedian_id numeric not null);");
        dao = handle.attach(MovieDao.class);
    }

    @Test
    public void shouldGetMovieWithActors() throws Exception {
        Movie jeans = Movie.builder().movieId(1).movieName("Jeans").build();
        Actor hero = Actor.builder().actorId(1).actorName("Prasanth").build();
        Actor comedian = Actor.builder().actorId(2).actorName("Senthil").build();

        insert(jeans);
        insert(hero, comedian);
        handle.execute("insert into movie_actor values(1,1,2)");

        Movie movie = dao.getMovie(1);

        assertEquals(new Integer(1), movie.getMovieId());
        assertEquals(hero, movie.getHero());
        assertEquals(comedian, movie.getComedian());

    }

    @After
    public void clearTables() throws Exception {
        handle.execute("drop table movie_actor");

    }
}

interface MovieDao {

    @SqlQuery("select m.*, h.actor_id AS hero$actorId, h.actor_name AS hero$actorName, " +
            "ca.actor_id AS comedian$actorId, ca.actor_name AS comedian$actorName  from movie m " +
            "join movie_actor ma on m.movie_id = ma.movie_id join actor h on h.actor_id = ma.hero_id " +
            "join actor ca on ca.actor_id = ma.comedian_id")
    public Movie getMovie(@Bind("movieId") Integer movieId);

}
