package in.folder.jdbi.model;

import in.folder.jdbi.annotations.PrimaryKey;
import lombok.*;
import lombok.experimental.Builder;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Director {
    @PrimaryKey
    private Integer directorId;
    private String directorName;
    private Integer movieId;

}
