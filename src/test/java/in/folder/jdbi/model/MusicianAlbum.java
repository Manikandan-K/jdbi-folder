package in.folder.jdbi.model;

import in.folder.jdbi.annotations.OneToMany;
import in.folder.jdbi.annotations.OneToOne;
import in.folder.jdbi.annotations.PrimaryKey;
import lombok.Getter;

import java.util.List;

@Getter
public class MusicianAlbum {
    @PrimaryKey
    @OneToOne(name="musician")
    private Musician musician;
    @OneToMany(name="album", type=Album.class)
    private List<Album> albums;
}
