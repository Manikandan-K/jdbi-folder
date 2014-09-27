package in.folder.jdbi.model;

import lombok.*;
import lombok.experimental.Builder;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@EqualsAndHashCode(of={"actorId"})
public class Actor {
    private Integer actorId;
    private String actorName;
    private Integer movieId;
}
