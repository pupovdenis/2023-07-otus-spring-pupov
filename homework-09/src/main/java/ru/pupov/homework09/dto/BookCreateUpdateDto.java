package ru.pupov.homework09.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

public record BookCreateUpdateDto(

        @NotBlank
        String name,

        @Positive
        @NotNull
        Long authorId,

        @Positive
        @NotNull
        Long genreId
) {

    @Builder
    public BookCreateUpdateDto {
    }
}
