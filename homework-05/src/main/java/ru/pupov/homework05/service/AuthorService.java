package ru.pupov.homework05.service;

import org.springframework.transaction.annotation.Transactional;
import ru.pupov.homework05.domain.Author;

import java.util.List;

public interface AuthorService {
    @Transactional
    Long insert(Author author);

    List<Author> getAll();

    Author getById(Long id);

    @Transactional
    void update(Author author);

    @Transactional
    boolean deleteById(Long id);
}
