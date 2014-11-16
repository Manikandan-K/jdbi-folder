package com.github.rkmk.model;

import com.github.rkmk.annotations.ColumnName;
import com.github.rkmk.annotations.PrimaryKey;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Builder;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Song {
    @PrimaryKey
    private Integer songId;
    @ColumnName("name")
    private String songName;
    private Integer movieId;
    private Integer albumId;
}
