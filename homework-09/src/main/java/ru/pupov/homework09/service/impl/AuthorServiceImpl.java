package ru.pupov.homework09.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pupov.homework09.dto.AuthorCreateDto;
import ru.pupov.homework09.dto.AuthorDto;
import ru.pupov.homework09.entity.Author;
import ru.pupov.homework09.mapper.AuthorMapper;
import ru.pupov.homework09.repository.AuthorRepository;
import ru.pupov.homework09.service.AuthorService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

    private final AuthorMapper authorMapper;

    @Override
    @Transactional
    public AuthorDto save(Author author) {
        var savedAuthor = authorRepository.save(author);
        return authorMapper.toDto(savedAuthor);
    }

    @Override
    @Transactional
    public AuthorDto update(Author author, String firstName, String lastName) {
        if (!firstName.isBlank()) {
            author.setFirstname(firstName);
        }
        if (!lastName.isBlank()) {
            author.setLastname(lastName);
        }
        var savedAuthor = authorRepository.save(author);
        return authorMapper.toDto(savedAuthor);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuthorDto> getAll() {
        var authors = authorRepository.findAll();
        return authors.stream()
                .map(authorMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        authorRepository.deleteById(id);
    }

    @Override
    public AuthorDto create(AuthorCreateDto authorCreateDto) {
        var author = authorMapper.toEntity(authorCreateDto);
        var savedAuthor = authorRepository.save(author);
        return authorMapper.toDto(savedAuthor);
    }
}
