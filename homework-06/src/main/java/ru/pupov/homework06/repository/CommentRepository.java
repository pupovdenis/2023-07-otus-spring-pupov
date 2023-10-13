package ru.pupov.homework06.repository;

import ru.pupov.homework06.entity.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentRepository {

    Comment save(Comment comment);

    Optional<Comment> findById(Long id);

    boolean deleteById(Long id);

    List<Comment> findByBookId(Long bookId);
}
