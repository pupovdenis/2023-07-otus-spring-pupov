package ru.pupov.homework05.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pupov.homework05.dao.GenreDao;
import ru.pupov.homework05.domain.Genre;
import ru.pupov.homework05.service.GenreService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class GenreServiceImpl implements GenreService {

    private final GenreDao genreDao;

    @Override
    @Transactional
    public Long insert(Genre genre) {
        if (genre.getId() != null) {
            log.error("before inserting genre must be without id");
            return null;
        }
        return genreDao.insert(genre);
    }

    @Override
    public List<Genre> getAll() {
        return genreDao.getAll();
    }

    @Override
    public Genre getById(Long id) {
        return genreDao.getById(id);
    }

    @Override
    @Transactional
    public void update(Genre genre) {
        genreDao.update(genre);
    }

    @Override
    @Transactional
    public boolean deleteById(Long id) {
        return genreDao.deleteById(id);
    }
}
