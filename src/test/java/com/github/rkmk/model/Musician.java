package com.github.rkmk.model;

import com.github.rkmk.annotations.OneToMany;
import com.github.rkmk.annotations.PrimaryKey;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Builder;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Musician {
    @PrimaryKey
    private Integer id ;
    private String name;
    @OneToMany("album")
    private List<Album> albums;
}
