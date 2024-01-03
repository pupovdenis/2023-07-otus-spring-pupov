package ru.pupov.homework09.service;


import ru.pupov.homework09.dto.GenreCreateDto;
import ru.pupov.homework09.dto.GenreDto;
import ru.pupov.homework09.entity.Genre;

import java.util.List;

public interface GenreService {

    GenreDto save(Genre genre);

    GenreDto update(Genre genre, String name);

    List<GenreDto> getAll();

    void deleteById(Long id);

    GenreDto create(GenreCreateDto genreCreateDto);
}
