package in.folder.jdbi.model;

import in.folder.jdbi.annotations.ColumnName;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Team {
    @ColumnName("id")
    private Integer teamId;
    @ColumnName("name")
    private String teamName;
}
