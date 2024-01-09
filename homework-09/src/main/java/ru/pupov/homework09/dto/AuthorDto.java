package ru.pupov.homework09.dto;

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
