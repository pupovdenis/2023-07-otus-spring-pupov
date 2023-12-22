package ru.pupov.homework08.service;

import ru.pupov.homework08.dto.AuthorDto;
import ru.pupov.homework08.model.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorService {
    AuthorDto save(Author author);

    List<AuthorDto> getAll();

    Optional<Author> findById(String authorId);

    Optional<AuthorDto> getById(String authorId);

    void deleteById(String authorId);
}
