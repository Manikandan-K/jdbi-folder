package in.folder.jdbi.dao;

import in.folder.jdbi.annotations.PrimaryKey;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Builder;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Level2 {
    @PrimaryKey
    private Integer id;
    private String stringCol;
    private Integer level1Id;
}
