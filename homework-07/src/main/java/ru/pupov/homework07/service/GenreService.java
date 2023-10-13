package ru.pupov.homework07.service;

import ru.pupov.homework07.dto.GenreDto;
import ru.pupov.homework07.entity.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreService {

    GenreDto save(Genre genre);

    GenreDto update(Genre genre, String name);

    List<GenreDto> getAll();

    Optional<Genre> findById(Long id);

    Optional<GenreDto> getById(Long id);

    void deleteById(Long id);
}
