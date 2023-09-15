package ru.pupov.homework05.dao;

import ru.pupov.homework05.domain.Author;

import java.util.List;

public interface AuthorDao {

    Long insert(Author author);

    Author getById(Long id);

    List<Author> getAll();

    void update(Author author, String bookIdsString);

    boolean deleteById(Long id);
}
