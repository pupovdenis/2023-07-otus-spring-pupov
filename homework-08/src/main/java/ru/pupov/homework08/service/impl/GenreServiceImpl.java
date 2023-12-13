package ru.pupov.homework08.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pupov.homework08.dto.GenreDto;
import ru.pupov.homework08.model.Genre;
import ru.pupov.homework08.mapper.GenreMapper;
import ru.pupov.homework08.repository.BookRepository;
import ru.pupov.homework08.repository.GenreRepository;
import ru.pupov.homework08.service.GenreService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;

    private final BookRepository bookRepository;

    private final GenreMapper genreMapper;

    @Override
    public GenreDto save(Genre genre) {
        var savedGenre = genreRepository.save(genre);
        return genreMapper.toDto(savedGenre);
    }

    @Override
    public List<GenreDto> getAll() {
        var genres = genreRepository.findAll();
        return genreMapper.toDtoList(genres);
    }

    @Override
    public Optional<Genre> findById(String id) {
        return genreRepository.findById(id);
    }

    @Override
    public Optional<GenreDto> getById(String id) {
        return genreRepository.findById(id)
                .map(genreMapper::toDto);
    }

    @Override
    @Transactional
    public void deleteById(String genreId) {
        bookRepository.deleteGenre(genreId);
        genreRepository.deleteById(genreId);
    }
}
