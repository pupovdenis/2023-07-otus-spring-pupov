package ru.pupov.homework05.dao;

import ru.pupov.homework05.domain.Book;

import java.util.List;

public interface BookDao {

    Long insert(Book book);

    Book getById(Long id);

    List<Book> getAll();

    void update(Book book);

    void deleteById(Long id);
}
