package in.folder.jdbi.model;

import in.folder.jdbi.annotations.ColumnName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Builder;

import java.math.BigDecimal;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Team {
    @ColumnName("id")
    private Integer teamId;
    @ColumnName("name")
    private String teamName;

    private BigDecimal average;
}
