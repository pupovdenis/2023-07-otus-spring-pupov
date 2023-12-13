package ru.pupov.homework08.dto;

import lombok.Builder;

public record CommentDto (
        String id,
        String text
) {

    @Builder
    public CommentDto {
    }
}