package in.folder.jdbi.model;

import in.folder.jdbi.annotations.OneToMany;
import in.folder.jdbi.annotations.OneToOne;
import lombok.*;
import lombok.experimental.Builder;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@EqualsAndHashCode(of = {"movieId"})
public class Movie {
    private Integer movieId;
    private String movieName;

    @OneToMany(name = "song", type = Song.class)
    private List<Song> songs;

    @OneToMany(name = "actor", type = Actor.class)
    private List<Actor> actors;

    @OneToOne(name = "director", type = Director.class)
    private Director director;

}
