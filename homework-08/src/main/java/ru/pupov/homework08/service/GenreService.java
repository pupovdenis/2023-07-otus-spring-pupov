package ru.pupov.homework08.service;

import ru.pupov.homework08.dto.GenreDto;
import ru.pupov.homework08.model.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreService {

    GenreDto save(Genre genre);

    List<GenreDto> getAll();

    Optional<Genre> findById(String id);

    Optional<GenreDto> getById(String id);

    void deleteById(String id);
}
