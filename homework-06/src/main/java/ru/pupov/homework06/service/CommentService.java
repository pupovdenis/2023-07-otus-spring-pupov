package ru.pupov.homework06.service;

import ru.pupov.homework06.dto.CommentDto;
import ru.pupov.homework06.entity.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentService {
    CommentDto save(Comment comment);

    CommentDto update(Comment comment, String text);

    Optional<Comment> findById(Long id);

    Optional<CommentDto> getById(Long id);

    boolean deleteById(Long id);

    List<CommentDto> getAllByBookId(Long bookId);
}
