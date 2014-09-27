package in.folder.jdbi.model;

import lombok.*;
import lombok.experimental.Builder;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@EqualsAndHashCode(of={"songId"})
public class Song {
    private Integer songId;
    private String songName;
    private Integer movieId;
}
