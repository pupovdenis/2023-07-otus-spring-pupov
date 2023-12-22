package ru.pupov.homework08.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pupov.homework08.dto.BookDto;
import ru.pupov.homework08.dto.CommentDto;
import ru.pupov.homework08.mapper.BookMapper;
import ru.pupov.homework08.mapper.CommentMapper;
import ru.pupov.homework08.model.Author;
import ru.pupov.homework08.model.Book;
import ru.pupov.homework08.model.Genre;
import ru.pupov.homework08.repository.BookRepository;
import ru.pupov.homework08.service.BookService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    private final BookMapper bookMapper;

    private final CommentMapper commentMapper;

    @Override
    @Transactional
    public BookDto save(Book book) {
        var savedBook = bookRepository.save(book);
        return bookMapper.toDto(savedBook);
    }

    @Override
    @Transactional
    public BookDto update(Book book, String name, Author author, Genre genre) {
        if (name != null && !name.isBlank()) {
            book.setName(name);
        }
        if (author != null) {
            book.setAuthor(author);
        }
        if (genre != null) {
            book.setGenre(genre);
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
    public Optional<BookDto> getById(String id) {
        return bookRepository.findById(id)
                .map(bookMapper::toDto);
    }

    @Override
    public Optional<Book> findById(String id) {
        return bookRepository.findById(id);
    }

    @Override
    public List<BookDto> getAllByAuthorId(String authorId) {
        var books = bookRepository.findByAuthorId(authorId);
        return bookMapper.toDtoListWithoutAuthor(books);
    }

    @Override
    public List<BookDto> getAllByGenreId(String genreId) {
        var books = bookRepository.findByGenreId(genreId);
        return bookMapper.toDtoListWithoutGenre(books);
    }

    @Override
    public void deleteById(String id) {
        bookRepository.deleteById(id);
    }

    @Override
    public List<CommentDto> getCommentsByBookId(String bookId) {
        return bookRepository.findById(bookId)
                .map(Book::getComments)
                .map(commentMapper::toDtoList)
                .orElse(Collections.emptyList());
    }

    @Override
    public boolean updateCommentById(String bookId, String commentId, String text) {
        return bookRepository.updateCommentById(bookId, commentId, text);
    }

    @Override
    public boolean deleteCommentByIds(String bookId, List<String> commentIds) {
        return bookRepository.deleteCommentByIds(bookId, commentIds);
    }

    @Override
    public boolean addComment(String bookId, String text) {
        return bookRepository.addComment(bookId, text);
    }
}
