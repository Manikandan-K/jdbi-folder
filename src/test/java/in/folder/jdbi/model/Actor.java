package in.folder.jdbi.model;

import lombok.*;
import lombok.experimental.Builder;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@EqualsAndHashCode(of={"actorId"})
public class Actor {
    private Integer actorId;
    private String actorName;
    private Integer movieId;
}
