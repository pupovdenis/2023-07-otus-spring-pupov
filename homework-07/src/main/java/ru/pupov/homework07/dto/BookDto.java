package ru.pupov.homework07.dto;

import lombok.Builder;

import java.util.List;

public record BookDto(
        Long id,

        String name,

        AuthorDto author,

        GenreDto genre,

        List<CommentDto> comments
) {

    @Builder
    public BookDto {
    }
}
