package ru.pupov.homework09.service;


import ru.pupov.homework09.dto.BookCreateUpdateDto;
import ru.pupov.homework09.dto.BookDto;

import java.util.List;
import java.util.Optional;

public interface BookService {

    BookDto create(BookCreateUpdateDto bookCreateDto);

    BookDto update(Long bookId, String name, Long authorId, Long genreId);

    List<BookDto> getAll();

    Optional<BookDto> getById(Long id);

    List<BookDto> getAllByAuthorId(Long authorId);

    List<BookDto> getAllByGenreId(Long genreId);

    void deleteById(Long id);
}
