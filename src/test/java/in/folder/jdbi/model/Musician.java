package in.folder.jdbi.model;

import in.folder.jdbi.annotations.PrimaryKey;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Builder;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Musician {
    @PrimaryKey
    private Integer id ;
    private String name;
}
