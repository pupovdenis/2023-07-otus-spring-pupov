package ru.pupov.homework08.repository;

import java.util.List;

public interface BookRepositoryCustom {

    boolean updateCommentById(String bookId, String commentId, String text);

    boolean deleteCommentByIds(String bookId, List<String> commentsIds);

    boolean addComment(String bookId, String text);

    void deleteAuthor(String authorId);

    void deleteGenre(String genreId);
}
