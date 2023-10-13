package ru.pupov.homework06.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.pupov.homework06.dto.BookDto;
import ru.pupov.homework06.entity.Book;
import ru.pupov.homework06.service.AuthorService;
import ru.pupov.homework06.service.BookService;
import ru.pupov.homework06.service.GenreService;

import java.util.Collections;
import java.util.List;

@ShellComponent
@RequiredArgsConstructor
public class BookCommands {

    private final BookService bookService;

    private final AuthorService authorService;

    private final GenreService genreService;

    private final ConversionService conversionService;

    @ShellMethod(value = "Create book", key = {"create-book", "cb"})
    public String create(@ShellOption(help = "Get book's name") String name,
                         @ShellOption(help = "Get author id") Long authorId,
                         @ShellOption(help = "Get genre id") Long genreId) {
        if (name.isBlank()) {
            return "Please, enter book name";
        }
        var optionalAuthor = authorService.findById(authorId);
        if (optionalAuthor.isEmpty()) {
            return "Could not find author id %d".formatted(authorId);
        }
        var optionalGenre = genreService.findById(genreId);
        if (optionalGenre.isEmpty()) {
            return "Could not find genre id %d".formatted(genreId);
        }
        var book = new Book(null, name, optionalAuthor.get(), optionalGenre.get(), Collections.emptyList());
        var savedBookDto = bookService.save(book);
        return savedBookDto == null ? "Failed to create book" : "Book id %d was created".formatted(savedBookDto.id());
    }

    @ShellMethod(value = "Get book by id", key = {"read-book", "rb"})
    public String readById(@ShellOption(help = "Get id of the book") Long id) {
        return bookService.getById(id)
                .map(bookDto -> getResponseFrom(List.of(bookDto)))
                .orElse("Could not find the book id %d".formatted(id));
    }

    @ShellMethod(value = "Get books", key = {"read-all-books", "rbs"})
    public String readAll() {
        var bookDtoList = bookService.getAll();
        return getResponseFrom(bookDtoList);
    }

    @ShellMethod(value = "Get books by author id", key = {"read-books-by-author-id", "rba"})
    public String readAllByAuthorId(@ShellOption(help = "Get id of the author") Long authorId) {
        var bookDtoList = bookService.getAllByAuthorId(authorId);
        return getResponseFrom(bookDtoList);
    }

    @ShellMethod(value = "Get books by genre id", key = {"read-books-by-genre-id", "rbg"})
    public String readAllByGenreId(@ShellOption(help = "Get id of genre") Long genreId) {
        var bookDtoList = bookService.getAllByGenreId(genreId);
        return getResponseFrom(bookDtoList);
    }

    @ShellMethod(value = "Update book", key = {"update-book", "ub"})
    public String update(@ShellOption(help = "Get id of the book") Long id,
                         @ShellOption(help = "Get new name") String name,
                         @ShellOption(help = "Get new author_id", value = "--author") Long authorId,
                         @ShellOption(help = "Get new genre_id", value = "--genre") Long genreId) {
        var optionalBook = bookService.findById(id);
        if (optionalBook.isEmpty()) {
            return "Could not find the book id %d".formatted(id);
        }
        var updatedBookDto = bookService.update(optionalBook.get(), name, authorId, genreId);
        return updatedBookDto == null
                ? "Failed to create book"
                : "Book id %d has been updated".formatted(updatedBookDto.id());
    }

    @ShellMethod(value = "Delete book", key = {"delete-book", "db"})
    public String deleteById(@ShellOption(help = "Get id of the book") Long id) {
        var result = bookService.deleteById(id);
        return result ? "Book id %d was deleted".formatted(id) : "Could not find book id %s".formatted(id);
    }

    private String getResponseFrom(List<BookDto> books) {
        return conversionService.convert(books, String.class);
    }
}
