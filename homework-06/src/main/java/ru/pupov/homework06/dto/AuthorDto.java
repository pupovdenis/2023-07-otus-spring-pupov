package ru.pupov.homework06.dto;

import lombok.Builder;

public record AuthorDto(
        Long id,

        String firstname,

        String lastname
) {

    @Builder
    public AuthorDto {
    }
}
