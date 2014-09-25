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
@EqualsAndHashCode(of = {"movie_id"})
public class Movie {
    Integer movieId;
    String movieName;

    @OneToMany(name = "song", type = Song.class)
    List<Song> songs;
}
