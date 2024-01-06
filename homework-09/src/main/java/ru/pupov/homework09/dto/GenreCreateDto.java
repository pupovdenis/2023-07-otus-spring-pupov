package ru.pupov.homework09.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

public record GenreCreateDto(

        @NotBlank
        String name
) {

    @Builder
    public GenreCreateDto {
    }
}
