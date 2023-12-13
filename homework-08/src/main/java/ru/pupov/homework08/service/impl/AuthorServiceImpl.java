package ru.pupov.homework08.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pupov.homework08.dto.AuthorDto;
import ru.pupov.homework08.mapper.AuthorMapper;
import ru.pupov.homework08.model.Author;
import ru.pupov.homework08.repository.AuthorRepository;
import ru.pupov.homework08.repository.BookRepository;
import ru.pupov.homework08.service.AuthorService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

    private final BookRepository bookRepository;

    private final AuthorMapper authorMapper;

    @Override
    public AuthorDto save(Author author) {
        var savedAuthor = authorRepository.save(author);
        return authorMapper.toDto(savedAuthor);
    }

    @Override
    public List<AuthorDto> getAll() {
        var authors = authorRepository.findAll();
        return authorMapper.toDtoList(authors);
    }

    @Override
    public Optional<AuthorDto> getById(String id) {
        return authorRepository.findById(id)
                .map(authorMapper::toDto);
    }

    @Override
    public Optional<Author> findById(String id) {
        return authorRepository.findById(id);
    }

    @Override
    @Transactional
    public void deleteById(String authorId) {
        bookRepository.deleteAuthor(authorId);
        authorRepository.deleteById(authorId);
    }
}
