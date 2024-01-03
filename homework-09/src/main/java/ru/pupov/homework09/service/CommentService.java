package ru.pupov.homework09.service;


import ru.pupov.homework09.dto.CommentDto;
import ru.pupov.homework09.entity.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentService {
    CommentDto save(Comment comment);

    CommentDto update(Long commentId, String text);

    Optional<Comment> findById(Long id);

    void deleteById(Long id);

    List<CommentDto> getAllByBookId(Long bookId);

    CommentDto add(CommentDto commentDto);
}
