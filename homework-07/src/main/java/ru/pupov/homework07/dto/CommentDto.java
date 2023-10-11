package ru.pupov.homework07.dto;

import lombok.Builder;

public record CommentDto (
        Long id,

        String text
) {

    @Builder
    public CommentDto {
    }
}