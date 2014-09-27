package in.folder.jdbi.model;

import lombok.*;
import lombok.experimental.Builder;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@EqualsAndHashCode(of={"directorId"})
public class Director {
    private Integer directorId;
    private String directorName;
    private Integer movieId;

}
