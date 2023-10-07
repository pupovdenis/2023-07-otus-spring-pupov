package ru.pupov.homework06.service;

import ru.pupov.homework06.dto.AuthorDto;
import ru.pupov.homework06.entity.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorService {
    AuthorDto save(Author author);

    AuthorDto update(Author author, String firstName, String lastName);

    List<AuthorDto> getAll();

    Optional<Author> findById(Long id);

    Optional<AuthorDto> getById(Long id);

    boolean deleteById(Long id);
}
