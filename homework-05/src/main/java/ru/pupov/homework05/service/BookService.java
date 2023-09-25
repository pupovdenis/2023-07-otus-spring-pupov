package ru.pupov.homework05.service;

import org.springframework.transaction.annotation.Transactional;
import ru.pupov.homework05.domain.Book;

import java.util.List;

public interface BookService {
    @Transactional
    Long create(Book book);

    List<Book> getAll();

    Book getById(Long id);

    List<Book> getAllByAuthorId(Long authorId);

    List<Book> getAllByGenreId(Long genreId);

    @Transactional
    boolean deleteById(Long id);

    @Transactional
    void update(Book book);
}
