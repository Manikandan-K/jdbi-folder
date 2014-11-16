package com.github.rkmk.dao;

import com.github.rkmk.DaoTest;
import com.github.rkmk.model.Album;
import com.github.rkmk.model.Musician;
import com.github.rkmk.model.Song;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class MusicianDaoTest extends DaoTest{

    private Dao dao;

    @Before
    public void before() {
        dao = handle.attach(Dao.class);
    }

    @Test
    public void shouldGetMusicianAlongWithAlbumAndSongs() throws Exception {
        dataSetupForRahman();

        List<Musician> musicians = dao.getMusician();

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

        List<Musician> musicians = dao.getMusician();

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
