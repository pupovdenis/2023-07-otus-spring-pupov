package ru.pupov.homework09.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

public record AuthorCreateDto(

        @NotBlank
        String firstname,

        @NotBlank
        String lastname
) {

    @Builder
    public AuthorCreateDto {
    }
}
