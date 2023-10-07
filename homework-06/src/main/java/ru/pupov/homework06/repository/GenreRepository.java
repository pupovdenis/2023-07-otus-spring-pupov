package ru.pupov.homework06.repository;

import ru.pupov.homework06.entity.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreRepository {

    Genre save(Genre genre);

    Optional<Genre> findById(Long id);

    List<Genre> findAll();

    boolean deleteById(Long id);
}
