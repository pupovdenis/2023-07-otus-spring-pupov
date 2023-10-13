package ru.pupov.homework06.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pupov.homework06.dto.AuthorDto;
import ru.pupov.homework06.entity.Author;
import ru.pupov.homework06.mapper.AuthorMapper;
import ru.pupov.homework06.repository.AuthorRepository;
import ru.pupov.homework06.service.AuthorService;

import java.util.List;
import java.util.Optional;

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
        return authorMapper.toDtoList(authors);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AuthorDto> getById(Long id) {
        return authorRepository.findById(id)
                .map(authorMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Author> findById(Long id) {
        return authorRepository.findById(id);
    }

    @Override
    @Transactional
    public boolean deleteById(Long id) {
        return authorRepository.deleteById(id);
    }
}
