package in.folder.jdbi.model;

import in.folder.jdbi.annotations.OneToMany;
import in.folder.jdbi.annotations.OneToOne;
import in.folder.jdbi.annotations.PrimaryKey;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Builder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Movie {
    @PrimaryKey
    private Integer movieId;
    private String movieName;

    @OneToMany(name = "song")
    private ArrayList<Song> songs;

    @OneToMany(name = "actor")
    private List<Actor> actors;

    @OneToOne(name = "director")
    private Director director;

    private BigDecimal ratings;

}
