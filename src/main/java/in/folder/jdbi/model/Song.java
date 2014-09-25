package in.folder.jdbi.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Builder;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Song {
    Integer songId;
    String songName;
    Integer movieId;
}
