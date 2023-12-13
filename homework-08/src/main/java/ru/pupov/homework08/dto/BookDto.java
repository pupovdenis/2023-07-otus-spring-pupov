package ru.pupov.homework08.dto;

import lombok.Builder;

import java.util.List;

public record BookDto(
        String id,
        String name,
        AuthorDto author,
        GenreDto genre,
        List<CommentDto> comments
) {

    @Builder
    public BookDto {
    }
}
