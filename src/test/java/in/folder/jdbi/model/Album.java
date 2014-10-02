package in.folder.jdbi.model;

import in.folder.jdbi.annotations.PrimaryKey;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Builder;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Album {
    @PrimaryKey
    private Integer id;
    private String name;
    private Integer musicianId;
}
