package ru.pupov.homework06.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pupov.homework06.dto.BookDto;
import ru.pupov.homework06.entity.Author;
import ru.pupov.homework06.entity.Book;
import ru.pupov.homework06.entity.Genre;
import ru.pupov.homework06.mapper.BookMapper;
import ru.pupov.homework06.repository.BookRepository;
import ru.pupov.homework06.service.BookService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    private final BookMapper bookMapper;

    @Override
    @Transactional
    public BookDto save(Book book) {
        var savedBook = bookRepository.save(book);
        return bookMapper.toDto(savedBook);
    }

    @Override
    @Transactional
    public BookDto update(Book book, String name, Long authorId, Long genreId) {
        if (name != null && !name.isBlank()) {
            book.setName(name);
        }
        if (book.getAuthor() != null) {
            book.setAuthor(Author.builder()
                    .id(authorId)
                    .build());
        }
        if (book.getGenre() != null) {
            book.setGenre(Genre.builder()
                    .id(genreId)
                    .build());
        }
        var savedBook = bookRepository.save(book);
        return bookMapper.toDto(savedBook);
    }

    @Override
    public List<BookDto> getAll() {
        var books = bookRepository.findAll();
        return bookMapper.toDtoList(books);
    }

    @Override
    public Optional<BookDto> getById(Long id) {
        return bookRepository.findById(id)
                .map(bookMapper::toDto);
    }

    @Override
    public Optional<Book> findById(Long id) {
        return bookRepository.findById(id);
    }

    @Override
    public List<BookDto> getAllByAuthorId(Long authorId) {
        var books = bookRepository.findByAuthorId(authorId);
        return bookMapper.toDtoListWithoutAuthor(books);
    }

    @Override
    public List<BookDto> getAllByGenreId(Long genreId) {
        var books = bookRepository.findByGenreId(genreId);
        return bookMapper.toDtoListWithoutGenre(books);
    }

    @Override
    @Transactional
    public boolean deleteById(Long id) {
        return bookRepository.deleteById(id);
    }
}
