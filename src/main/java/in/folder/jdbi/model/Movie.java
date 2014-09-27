package in.folder.jdbi.model;

import in.folder.jdbi.annotations.OneToMany;
import lombok.*;
import lombok.experimental.Builder;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@EqualsAndHashCode(of = {"movieId"})
public class Movie {
    private Integer movieId;
    private String movieName;

    @OneToMany(name = "song", type = Song.class)
    List<Song> songs;

    @OneToMany(name = "actor", type = Actor.class)
    List<Actor> actors;
}
