package ru.pupov.homework06.dto;

import lombok.Builder;

public record GenreDto (
        Long id,

        String name
) {

    @Builder
    public GenreDto {
    }
}
