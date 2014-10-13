package in.folder.jdbi.model;

import in.folder.jdbi.annotations.ColumnName;
import in.folder.jdbi.annotations.PrimaryKey;
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
