package ru.pupov.homework05.service;

import org.springframework.transaction.annotation.Transactional;
import ru.pupov.homework05.domain.Genre;

import java.util.List;

public interface GenreService {
    @Transactional
    Long insert(Genre genre);

    List<Genre> getAll();

    Genre getById(Long id);

    @Transactional
    void update(Genre genre);

    @Transactional
    boolean deleteById(Long id);
}
