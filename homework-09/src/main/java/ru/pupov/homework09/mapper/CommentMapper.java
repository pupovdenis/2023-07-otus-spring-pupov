package ru.pupov.homework09.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.pupov.homework09.dto.CommentDto;
import ru.pupov.homework09.entity.Comment;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(source = "book.id", target = "bookId")
    CommentDto toDto(Comment comment);

    @Mapping(source = "bookId", target = "book.id")
    Comment toEntity(CommentDto commentDto);
}