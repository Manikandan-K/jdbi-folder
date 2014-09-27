package in.folder.jdbi.model;

import lombok.*;
import lombok.experimental.Builder;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@EqualsAndHashCode(of={"directorId"})
public class Director {
    private Integer directorId;
    private String directorName;
    private Integer movieId;

}
