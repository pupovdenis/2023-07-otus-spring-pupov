package ru.pupov.homework09.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pupov.homework09.dto.GenreCreateDto;
import ru.pupov.homework09.dto.GenreDto;
import ru.pupov.homework09.entity.Genre;
import ru.pupov.homework09.mapper.GenreMapper;
import ru.pupov.homework09.repository.GenreRepository;
import ru.pupov.homework09.service.GenreService;

import java.util.List;

@Service
@RequiredArgsConstructor
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
    @Transactional(readOnly = true)
    public List<GenreDto> getAll() {
        var genres = genreRepository.findAll();
        return genres.stream()
                .map(genreMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        genreRepository.deleteById(id);
    }

    @Override
    public GenreDto create(GenreCreateDto genreCreateDto) {
        var author = genreMapper.toEntity(genreCreateDto);
        var savedAuthor = genreRepository.save(author);
        return genreMapper.toDto(savedAuthor);
    }
}
