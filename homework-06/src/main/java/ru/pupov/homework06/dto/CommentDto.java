package ru.pupov.homework06.dto;

import lombok.Builder;

public record CommentDto (
        Long id,

        String text
) {

    @Builder
    public CommentDto {
    }
}