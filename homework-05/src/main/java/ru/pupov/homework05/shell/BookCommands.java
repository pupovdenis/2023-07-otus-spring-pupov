package ru.pupov.homework05.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.pupov.homework05.domain.Author;
import ru.pupov.homework05.domain.Book;
import ru.pupov.homework05.domain.Genre;
import ru.pupov.homework05.service.BookService;

import java.util.List;

@ShellComponent
@RequiredArgsConstructor
public class BookCommands {

    private final BookService bookService;

    private final ConversionService conversionService;

    @ShellMethod(value = "Create book", key = {"create-book", "cb"})
    public String create(@ShellOption(help = "Get book's name") String name,
                         @ShellOption(help = "Get author id") Long authorId,
                         @ShellOption(help = "Get genre id") Long genreId) {
        if (authorId == null || genreId == null) {
            return "Please, enter author id and genre id";
        }
        var book = new Book(null, name,
                new Author(authorId, null, null),
                new Genre(genreId, null));
        var id = bookService.create(book);
        return id == null ? "Failed to create book" : "Book id %d was created".formatted(id);
    }

    @ShellMethod(value = "Get book by id", key = {"read-book", "rb"})
    public String readById(@ShellOption(help = "Get id of the book") Long id) {
        var book = bookService.getById(id);
        return book == null ? "null" : getResponseFrom(List.of(book));
    }

    @ShellMethod(value = "Get books", key = {"read-all-books", "rbs"})
    public String readAll() {
        var books = bookService.getAll();
        return getResponseFrom(books);
    }

    @ShellMethod(value = "Get book(s) by author id", key = {"read-books-by-author-id", "rba"})
    public String readAllByAuthorId(
            @ShellOption(help = "Get id of the book by author id") Long authorId) {
        var books = bookService.getAllByAuthorId(authorId);
        return getResponseFrom(books);
    }

    @ShellMethod(value = "Get book(s) by genre id", key = {"read-books-by-genre-id", "rbg"})
    public String readAllByGenreId(
            @ShellOption(help = "Get id of the book by genre id") Long genreId) {
        var book = bookService.getAllByGenreId(genreId);
        return getResponseFrom(book);
    }

    @ShellMethod(value = "Update book", key = {"update-book", "ub"})
    public String update(
            @ShellOption(help = "Get id of the book") Long id,
            @ShellOption(help = "Get new name") String name,
            @ShellOption(help = "Get new author_id", value = "--author") Long authorId,
            @ShellOption(help = "Get new genre_id", value = "--genre") Long genreId) {
        var book = bookService.getById(id);
        if (book == null) {
            return "Could not find the book id %d".formatted(id);
        }
        bookService.update(book, name, authorId, genreId);
        return "Book id %d has been updated".formatted(book.getId());
    }

    @ShellMethod(value = "Delete book", key = {"delete-book", "db"})
    public String deleteById(@ShellOption(help = "Get id of the book") Long id) {
        var result = bookService.deleteById(id);
        return result ? "Book id %d was deleted".formatted(id) : "Could not find book id %s".formatted(id);
    }

    private String getResponseFrom(List<Book> books) {
        return conversionService.convert(books, String.class);
    }
}
