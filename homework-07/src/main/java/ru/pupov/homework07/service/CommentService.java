package ru.pupov.homework07.service;

import ru.pupov.homework07.dto.CommentDto;
import ru.pupov.homework07.entity.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentService {
    CommentDto save(Comment comment);

    CommentDto update(Comment comment, String text);

    Optional<Comment> findById(Long id);

    Optional<CommentDto> getById(Long id);

    void deleteById(Long id);

    List<CommentDto> getAllByBookId(Long bookId);
}
