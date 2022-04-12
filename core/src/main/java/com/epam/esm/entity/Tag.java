package com.epam.esm.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Tag {
    private Long id;
    private String name;

    @JsonCreator
    public Tag() {
    }
}
