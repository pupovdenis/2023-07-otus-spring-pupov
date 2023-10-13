package ru.pupov.homework07.mapper;

import org.springframework.stereotype.Component;
import ru.pupov.homework07.dto.CommentDto;
import ru.pupov.homework07.entity.Comment;

import java.util.List;

@Component
public class CommentMapper {

    public List<CommentDto> toDtoList(List<Comment> comments) {
        return comments.stream()
                .map(this::toDto)
                .toList();
    }

    public CommentDto toDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .build();
    }
}
