package ru.pupov.homework09.service;


import ru.pupov.homework09.dto.AuthorCreateDto;
import ru.pupov.homework09.dto.AuthorDto;
import ru.pupov.homework09.entity.Author;

import java.util.List;

public interface AuthorService {
    AuthorDto save(Author author);

    AuthorDto update(Author author, String firstName, String lastName);

    List<AuthorDto> getAll();

    void deleteById(Long id);

    AuthorDto create(AuthorCreateDto authorCreateDto);
}
