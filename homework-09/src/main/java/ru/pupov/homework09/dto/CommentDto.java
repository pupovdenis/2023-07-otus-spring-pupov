package ru.pupov.homework09.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

public record CommentDto (
        Long id,

        @NotBlank
        @Size(min = 1, max = 255)
        String text,

        Long bookId
) {

    @Builder
    public CommentDto {
    }
}