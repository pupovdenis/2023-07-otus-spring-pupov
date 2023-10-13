package ru.pupov.homework06.repository;

import ru.pupov.homework06.entity.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorRepository {

    Author save(Author author);

    Optional<Author> findById(Long id);

    List<Author> findAll();

    boolean deleteById(Long id);
}
