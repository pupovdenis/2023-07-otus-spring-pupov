package ru.pupov.homework09.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pupov.homework09.dto.BookCreateUpdateDto;
import ru.pupov.homework09.dto.BookDto;
import ru.pupov.homework09.entity.Author;
import ru.pupov.homework09.entity.Genre;
import ru.pupov.homework09.mapper.BookMapper;
import ru.pupov.homework09.repository.BookRepository;
import ru.pupov.homework09.service.BookService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    private final BookMapper bookMapper;

    @Override
    @Transactional
    public BookDto create(BookCreateUpdateDto bookCreateDto) {
        var book = bookMapper.toEntity(bookCreateDto);
        var savedBook = bookRepository.save(book);
        return bookMapper.toDto(savedBook);
    }

    @Override
    @Transactional
    public BookDto update(Long bookId, String name, Long authorId, Long genreId) {
        var book = bookRepository.findById(bookId).orElse(null);
        if (book != null) {
            if (name != null && !name.isBlank()) {
                book.setName(name);
            }
            if (authorId != null) {
                book.setAuthor(Author.builder()
                        .id(authorId)
                        .build());
            }
            if (genreId != null) {
                book.setGenre(Genre.builder()
                        .id(genreId)
                        .build());
            }
            book = bookRepository.save(book);
        }
        return bookMapper.toDto(book);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookDto> getAll() {
        var books = bookRepository.findAll();
        return books.stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BookDto> getById(Long id) {
        return bookRepository.findById(id)
                .map(bookMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookDto> getAllByAuthorId(Long authorId) {
        var books = bookRepository.findByAuthorId(authorId);
        return books.stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookDto> getAllByGenreId(Long genreId) {
        var books = bookRepository.findByGenreId(genreId);
        return books.stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }
}
