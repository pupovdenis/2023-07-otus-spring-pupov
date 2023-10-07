package ru.pupov.homework06.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pupov.homework06.dto.GenreDto;
import ru.pupov.homework06.entity.Genre;
import ru.pupov.homework06.mapper.GenreMapper;
import ru.pupov.homework06.repository.GenreRepository;
import ru.pupov.homework06.service.GenreService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;

    private final GenreMapper genreMapper;

    @Override
    @Transactional
    public GenreDto save(Genre genre) {
        var savedGenre = genreRepository.save(genre);
        return genreMapper.toDto(savedGenre);
    }

    @Override
    @Transactional
    public GenreDto update(Genre genre, String name) {
        if (!name.isBlank()) {
            genre.setName(name);
        }
        var savedGenre = genreRepository.save(genre);
        return genreMapper.toDto(savedGenre);
    }

    @Override
    public List<GenreDto> getAll() {
        var genres = genreRepository.findAll();
        return genreMapper.toDtoList(genres);
    }

    @Override
    public Optional<Genre> findById(Long id) {
        return genreRepository.findById(id);
    }

    @Override
    public Optional<GenreDto> getById(Long id) {
        return genreRepository.findById(id)
                .map(genreMapper::toDto);
    }

    @Override
    @Transactional
    public boolean deleteById(Long id) {
        return genreRepository.deleteById(id);
    }
}
