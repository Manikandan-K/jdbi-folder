package in.folder.jdbi.dao;

import in.folder.jdbi.DaoTest;
import in.folder.jdbi.container.FoldingList;
import in.folder.jdbi.model.Director;
import in.folder.jdbi.model.Movie;
import in.folder.jdbi.model.Song;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SqlObjectFolderTest extends DaoTest {
    private SqlObjectDao dao;

    @Before
    public void setUp() {
        dao = handle.attach(SqlObjectDao.class);
    }

    @Test
    public void shouldGetMovies() throws Exception {
        Movie jeans = Movie.builder().movieId(1).movieName("Jeans").build();
        Song song1 = Song.builder().songId(1).songName("Anbe Anbe").movieId(1).build();
//        Song song2 = Song.builder().songId(2).songName("Columbus Columbus").movieId(1).build();
//        Song song3 = Song.builder().songId(3).songName("Enake Enaka").movieId(1).build();
//        Song song4 = Song.builder().songId(4).songName("Poovukul").movieId(1).build();
        Director shankar = Director.builder().directorId(1).directorName("Shankar").movieId(1).build();

        insert(jeans);
        insert(song1);
        insert(shankar);

        FoldingList<Movie> movies = dao.getMovie();

        assertEquals(1, movies.getValues().size());
    }
}
