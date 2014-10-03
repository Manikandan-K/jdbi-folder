package in.folder.jdbi.dao;

import in.folder.jdbi.DaoTest;
import in.folder.jdbi.model.Movie;
import in.folder.jdbi.model.Song;
import in.folder.jdbi.model.Team;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class SqlObjectDaoTest extends DaoTest{
    private SqlObjectDao dao;

    @Before
    public void setUp() {
        dao = handle.attach(SqlObjectDao.class);
    }

    @Test(expected = RuntimeException.class)
    public void shouldTakeRegisteredMapper() throws Exception {
        Movie jeans = Movie.builder().movieId(1).movieName("Jeans").build();
        insert(jeans);

        dao.getMovies();
    }

    @Test
    public void shouldUseOfCustomMappingFactory() throws Exception {
        Song song1 = Song.builder().songId(1).songName("Anbe Anbe").build();
        Song song2 = Song.builder().songId(2).songName("Columbus Columbus").build();
        insert(song1, song2);

        List<Song> songs = dao.getSongs();

        assertEquals(2, songs.size());
    }

    @Test
    public void shouldUseColumnNameWhileMapping() throws Exception {
        Team csk = new Team(1, "CSK");
        Team rcb = new Team(2, "RCB");
        Team mi = new Team(3, "MI");
        insert(csk, rcb, mi);

        List<Team> teams = dao.getTeam();

        assertEquals(3, teams.size());
        assertEquals(csk, teams.get(0));
        assertEquals(rcb, teams.get(1));
        assertEquals(mi, teams.get(2));
    }
}
