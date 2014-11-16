package com.github.rkmk.model;

import com.github.rkmk.annotations.PrimaryKey;
import lombok.*;
import lombok.experimental.Builder;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Actor {
    @PrimaryKey
    private Integer actorId;
    private String actorName;
    private Integer movieId;
}
