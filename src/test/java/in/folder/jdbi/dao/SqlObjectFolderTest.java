package in.folder.jdbi.dao;

import in.folder.jdbi.DaoTest;
import in.folder.jdbi.GenericFolder;
import in.folder.jdbi.mapper.BigDecimalMapperFactory;
import in.folder.jdbi.model.*;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SqlObjectFolderTest extends DaoTest {
    private SqlObjectDao dao;

    @Before
    public void setUp() {
        dao = handle.attach(SqlObjectDao.class);
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

        List<Movie> movies = dao.getMovie(1).getValues();

        assertEquals(1, movies.size());
        Movie movie = movies.get(0);
        assertEquals(new Integer(1), movie.getMovieId());
        assertEquals("Jeans", movie.getMovieName());
        assertEquals(4, movie.getSongs().size());
        List<String> songNames = movie.getSongs().stream().map(Song::getSongName).collect(toList());
        assertTrue(songNames.containsAll(Arrays.asList("Anbe Anbe", "Columbus Columbus", "Enake Enaka", "Poovukul")));
    }

    @Test
    public void shouldReturnNullWhenNoRecordIsFound() throws Exception {
        List<Movie> movies = dao.getMovie(1).getValues();

        assertEquals(0, movies.size());
    }


    @Test
    @Ignore("Empty list for left join")
    public void shouldGetMovieAloneIfThereIsNoSongs() throws Exception {
        Movie jeans = Movie.builder().movieId(1).movieName("Jeans").build();
        insert(jeans);

        Movie movie = dao.getMovie(1).getValues().get(0);

        assertEquals(new Integer(1), movie.getMovieId());
        assertEquals("Jeans", movie.getMovieName());
        assertEquals(0, movie.getSongs().size());
    }

    @Test
    public void shouldGetMultipleMovies() throws Exception {
        Movie jeans = Movie.builder().movieId(1).movieName("Jeans").build();
        Movie bombay = Movie.builder().movieId(2).movieName("Bombay").build();
        Song song1 = Song.builder().songId(1).songName("Anbe Anbe").movieId(1).build();
        Song song2 = Song.builder().songId(2).songName("Columbus Columbus").movieId(1).build();
        Song song3 = Song.builder().songId(3).songName("Enake Enaka").movieId(1).build();
        Song song4 = Song.builder().songId(3).songName("Kannalane").movieId(2).build();
        Song song5 = Song.builder().songId(4).songName("Uyire Uyire").movieId(2).build();

        insert(jeans, bombay);
        insert(song1, song2, song3, song4, song5);

        List<Movie> movies = dao.getAllMovies().getValues();

        assertEquals(2, movies.size());
        assertEquals("Jeans",movies.get(0).getMovieName());
        assertEquals(3, movies.get(0).getSongs().size());
        List<String> songNames = movies.get(0).getSongs().stream().map(Song::getSongName).collect(toList());
        assertTrue(songNames.containsAll(Arrays.asList("Anbe Anbe", "Columbus Columbus", "Enake Enaka")));

        assertEquals("Bombay", movies.get(1).getMovieName());
        assertEquals(2, movies.get(1).getSongs().size());
        songNames = movies.get(1).getSongs().stream().map(Song::getSongName).collect(toList());
        assertTrue(songNames.containsAll(Arrays.asList("Kannalane", "Uyire Uyire")));
    }


    @Test
    public void shouldGetMovieWithSongsAndActors() throws Exception {
        Movie jeans = Movie.builder().movieId(1).movieName("Jeans").build();
        Song song1 = Song.builder().songId(1).songName("Anbe Anbe").movieId(1).build();
        Song song2 = Song.builder().songId(2).songName("Columbus Columbus").movieId(1).build();
        Actor prashanth = Actor.builder().actorId(1).actorName("Prashanth").movieId(1).build();
        Actor aishwarya = Actor.builder().actorId(2).actorName("Aishwarya").movieId(1).build();

        insert(jeans);
        insert(song1, song2);
        insert(prashanth, aishwarya);

        Movie movie = dao.getMovie(1).getValues().get(0);

        assertEquals(2, movie.getSongs().size());
        List<String> songNames = movie.getSongs().stream().map(Song::getSongName).collect(toList());
        assertTrue(songNames.containsAll(Arrays.asList("Anbe Anbe", "Columbus Columbus")));
        assertEquals(2, movie.getActors().size());
        List<String> actorNames = movie.getActors().stream().map(Actor::getActorName).collect(toList());
        assertTrue(actorNames.containsAll(Arrays.asList("Prashanth", "Aishwarya")));
    }

    @Test
    public void shouldGetMovieAlongWithDirector() throws Exception {
        Movie jeans = Movie.builder().movieId(1).movieName("Jeans").build();
        Director shankar = Director.builder().directorId(1).directorName("Shankar").movieId(1).build();

        insert(jeans);
        insert(shankar);

        Movie movie = dao.getMovie(1).getValues().get(0);

        assertEquals("Jeans", movie.getMovieName());
        assertEquals(new Integer(1), movie.getDirector().getDirectorId());
        assertEquals("Shankar", movie.getDirector().getDirectorName());
    }

    @Test
    public void shouldTakeOverriddenMapperFactoryIfSpecified() throws Exception {
        Movie jeans = Movie.builder().movieId(1).movieName("Jeans").ratings(BigDecimal.ONE).build();
        insert(jeans);

        GenericFolder.register(new BigDecimalMapperFactory());

        Movie movie = dao.getMovie(1).getValues().get(0);

        assertEquals(BigDecimal.TEN, movie.getRatings());
    }

    @Test
    public void shouldGetMusicianAlongWithAlbumAndSongs() throws Exception {
        dataSetupForRahman();

        List<Musician> musicians = dao.getMusician().getValues();

        assertEquals(1, musicians.size());
        Musician musician = musicians.get(0);
        List<Album> albums = musician.getAlbums();
        assertEquals(2, albums.size());
        assertEquals("Roja", albums.get(0).getName());
        List<Song> songs1 = albums.get(0).getSongs();
        assertEquals(2, songs1.size());
        assertEquals("Kadhal Rojave", songs1.get(0).getSongName());
        assertEquals("Pudhu Vellai", songs1.get(1).getSongName());
        List<Song> songs2 = albums.get(1).getSongs();
        assertEquals(2, songs2.size());
        assertEquals("Kannalane", songs2.get(0).getSongName());
        assertEquals("Uyire", songs2.get(1).getSongName());
    }

    @Test
    public void shouldGetMultipleMusicians() throws Exception {
        dataSetupForRahman();
        dataSetupForIlayaraja();

        List<Musician> musicians = dao.getMusician().getValues();

        assertEquals(2, musicians.size());
        Musician rahman = musicians.get(0);
        assertEquals(2, rahman.getAlbums().size());
        assertEquals(2, rahman.getAlbums().get(0).getSongs().size());
        assertEquals(2, rahman.getAlbums().get(1).getSongs().size());
        Musician ilayaRaja = musicians.get(1);
        assertEquals(2, ilayaRaja.getAlbums().size());
        assertEquals(1, ilayaRaja.getAlbums().get(0).getSongs().size());
        assertEquals(2, ilayaRaja.getAlbums().get(1).getSongs().size());

    }

    private void dataSetupForRahman() {
        Musician rahman = Musician.builder().id(1).name("Rahman").build();

        Album roja = Album.builder().id(1).name("Roja").musicianId(1).build();
        Song song1 = Song.builder().songId(1).songName("Kadhal Rojave").albumId(1).build();
        Song song2 = Song.builder().songId(2).songName("Pudhu Vellai").albumId(1).build();

        Album bombay = Album.builder().id(2).name("Bombay").musicianId(1).build();
        Song song3 = Song.builder().songId(3).songName("Kannalane").albumId(2).build();
        Song song4 = Song.builder().songId(4).songName("Uyire").albumId(2).build();

        insert(rahman);
        insert(roja, bombay);
        insert(song1, song2, song3, song4);
    }

    private void dataSetupForIlayaraja() {
        Musician ilayaraja = Musician.builder().id(2).name("Ilayaraja").build();

        Album roja = Album.builder().id(3).name("16 vayathinile").musicianId(2).build();
        Song song1 = Song.builder().songId(5).songName("Senthoora poove").albumId(3).build();

        Album bombay = Album.builder().id(4).name("Mullum Malarum").musicianId(2).build();
        Song song2 = Song.builder().songId(6).songName("Senthalam poovil").albumId(4).build();
        Song song3 = Song.builder().songId(7).songName("Nitham nitham").albumId(4).build();

        insert(ilayaraja);
        insert(roja, bombay);
        insert(song1, song2, song3);
    }


}
