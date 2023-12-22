package ru.pupov.homework08.service;

import ru.pupov.homework08.dto.BookDto;
import ru.pupov.homework08.dto.CommentDto;
import ru.pupov.homework08.model.Author;
import ru.pupov.homework08.model.Book;
import ru.pupov.homework08.model.Genre;

import java.util.List;
import java.util.Optional;

public interface BookService {

    BookDto save(Book book);

    BookDto update(Book book, String name, Author author, Genre genre);

    List<BookDto> getAll();

    Optional<BookDto> getById(String id);

    Optional<Book> findById(String id);

    List<BookDto> getAllByAuthorId(String authorId);

    List<BookDto> getAllByGenreId(String genreId);

    void deleteById(String id);

    List<CommentDto> getCommentsByBookId(String bookId);

    boolean updateCommentById(String bookId, String commentId, String text);

    boolean deleteCommentByIds(String bookId, List<String> commentIds);

    boolean addComment(String bookId, String text);
}
