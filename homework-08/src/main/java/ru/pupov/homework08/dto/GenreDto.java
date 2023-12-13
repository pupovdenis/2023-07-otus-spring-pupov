package ru.pupov.homework08.dto;

import lombok.Builder;

public record GenreDto (
        String id,
        String name
) {

    @Builder
    public GenreDto {
    }
}
