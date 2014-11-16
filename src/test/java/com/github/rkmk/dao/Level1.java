package com.github.rkmk.dao;

import com.github.rkmk.annotations.ColumnName;
import com.github.rkmk.annotations.OneToMany;
import com.github.rkmk.annotations.PrimaryKey;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Builder;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Level1 {
    @PrimaryKey
    private Integer id;
    private String stringCol;
    @ColumnName("numeric_col")
    private Double numericCol;

    @OneToMany("levelTwo")
    private List<Level2> level2List;

}
