package ru.pupov.homework07.service;

import ru.pupov.homework07.dto.AuthorDto;
import ru.pupov.homework07.entity.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorService {
    AuthorDto save(Author author);

    AuthorDto update(Author author, String firstName, String lastName);

    List<AuthorDto> getAll();

    Optional<Author> findById(Long id);

    Optional<AuthorDto> getById(Long id);

    void deleteById(Long id);
}
