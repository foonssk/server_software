package com.example.rest.model;

import java.util.List;
import lombok.Data;
import jakarta.validation.constraints.NotNull;

@Data
public class Pet {
    @NotNull
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private Category category;

    @NotNull
    private List<Tag> tags;

    @NotNull
    private Status status;

    public Pet() {}

    public Pet(Long id, String name, Category category, List<Tag> tags, Status status) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.tags = tags;
        this.status = status;
    }
}
