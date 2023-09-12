package ru.pupov.homework05.dao;

import ru.pupov.homework05.domain.Genre;

import java.util.List;

public interface GenreDao {

    Long insert(Genre genre);

    Genre getById(Long id);

    List<Genre> getAll();

    void update(Genre genre, boolean withBooks);

    void deleteById(Long id);
}
