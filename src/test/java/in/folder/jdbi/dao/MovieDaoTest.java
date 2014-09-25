package in.folder.jdbi.dao;

import in.folder.jdbi.DaoTest;
import in.folder.jdbi.model.Movie;
import in.folder.jdbi.model.Song;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static junit.framework.Assert.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

public class MovieDaoTest extends DaoTest {
     private MovieDao dao;

    @Before
    public void before() {
        dao = handle.attach(MovieDao.class);
    }

    @Test
    public void shouldGetMovieWithSongs() throws Exception {
        Movie jeans = Movie.builder().movieId(1).movieName("Jeans").build();
        Song song1 = Song.builder().songId(1).songName("Anbe Anbe").movieId(1).build();
        Song song2 = Song.builder().songId(2).songName("Columbus Columbus").movieId(1).build();
        Song song3 = Song.builder().songId(3).songName("Enake Enaka").movieId(1).build();
        Song song4 = Song.builder().songId(4).songName("Poovukul").movieId(1).build();

        insert(jeans);
        insert(song1, song2,song3, song4);

        Movie movie = dao.getMovie(1);

        assertThat(movie.getMovieId(), is(1));
        assertThat(movie.getMovieName(), is("Jeans"));
        assertEquals(4, movie.getSongs().size());
        List<String> songNames = movie.getSongs().stream().map(Song::getSongName).collect(toList());
        assertTrue(songNames.containsAll(Arrays.asList("Anbe Anbe", "Columbus Columbus", "Enake Enaka", "Poovukul")));
    }

    @Test
    public void shouldReturnNullWhenNoRecordIsFound() throws Exception {
        Movie movie = dao.getMovie(1);

        assertNull(movie);
    }


}
