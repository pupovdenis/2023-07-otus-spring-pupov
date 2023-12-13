package ru.pupov.homework08.dto;

import lombok.Builder;

public record AuthorDto(
        String id,
        String firstname,
        String lastname
) {

    @Builder
    public AuthorDto {
    }
}
