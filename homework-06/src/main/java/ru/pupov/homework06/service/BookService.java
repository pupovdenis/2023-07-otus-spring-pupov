package ru.pupov.homework06.service;

import ru.pupov.homework06.dto.BookDto;
import ru.pupov.homework06.entity.Book;

import java.util.List;
import java.util.Optional;

public interface BookService {

    BookDto save(Book book);

    BookDto update(Book book, String name, Long authorId, Long genreId);

    List<BookDto> getAll();

    Optional<BookDto> getById(Long id);

    Optional<Book> findById(Long id);

    List<BookDto> getAllByAuthorId(Long authorId);

    List<BookDto> getAllByGenreId(Long genreId);

    boolean deleteById(Long id);
}
