package in.folder.jdbi.model;

import in.folder.jdbi.annotations.ColumnName;
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
    @ColumnName("name")
    private String movieName;

    @OneToMany("song")
    private ArrayList<Song> songs;

    @OneToMany("actor")
    private List<Actor> actors;

    @OneToOne("director")
    private Director director;

    private BigDecimal ratings;

}
