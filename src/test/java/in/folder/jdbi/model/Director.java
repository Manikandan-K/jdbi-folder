package in.folder.jdbi.model;

import in.folder.jdbi.annotations.OneToMany;
import in.folder.jdbi.annotations.PrimaryKey;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Builder;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Director {
    @PrimaryKey
    private Integer directorId;
    private String directorName;
    private Integer movieId;

    @OneToMany("assistant")
    private List<AssistantDirector> assistantDirectors;
}
