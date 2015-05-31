package com.github.rkmk.model;

import com.github.rkmk.annotations.ColumnName;
import com.github.rkmk.annotations.OneToMany;
import com.github.rkmk.annotations.OneToOne;
import com.github.rkmk.annotations.PrimaryKey;
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

    @OneToOne("hero")
    private Actor hero;
    @OneToOne("comedian")
    private Actor comedian;


    private BigDecimal ratings;

}
