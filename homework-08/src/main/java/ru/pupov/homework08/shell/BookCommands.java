package ru.pupov.homework08.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.pupov.homework08.dto.BookDto;
import ru.pupov.homework08.model.Book;
import ru.pupov.homework08.service.AuthorService;
import ru.pupov.homework08.service.BookService;
import ru.pupov.homework08.service.GenreService;

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
                         @ShellOption(help = "Get author id") String authorId,
                         @ShellOption(help = "Get genre id") String genreId) {
        if (name.isBlank()) {
            return "Please, enter book name";
        }
        var optionalAuthor = authorService.findById(authorId);
        if (optionalAuthor.isEmpty()) {
            return "Could not find author id %s".formatted(authorId);
        }
        var optionalGenre = genreService.findById(genreId);
        if (optionalGenre.isEmpty()) {
            return "Could not find genre id %s".formatted(genreId);
        }
        var book = new Book(name);
        book.setAuthor(optionalAuthor.get());
        book.setGenre(optionalGenre.get());
        var savedBookDto = bookService.save(book);
        return savedBookDto == null ? "Failed to create book" : "Book id %s was created".formatted(savedBookDto.id());
    }

    @ShellMethod(value = "Get book by id", key = {"read-book", "rb"})
    public String readById(@ShellOption(help = "Get id of the book") String id) {
        return bookService.getById(id)
                .map(bookDto -> getResponseFrom(List.of(bookDto)))
                .orElse("Could not find the book id %s".formatted(id));
    }

    @ShellMethod(value = "Get books", key = {"read-all-books", "rbs"})
    public String readAll() {
        var bookDtoList = bookService.getAll();
        return getResponseFrom(bookDtoList);
    }

    @ShellMethod(value = "Get books by author id", key = {"read-books-by-author-id", "rba"})
    public String readAllByAuthorId(@ShellOption(help = "Get id of the author") String authorId) {
        var bookDtoList = bookService.getAllByAuthorId(authorId);
        return getResponseFrom(bookDtoList);
    }

    @ShellMethod(value = "Get books by genre id", key = {"read-books-by-genre-id", "rbg"})
    public String readAllByGenreId(@ShellOption(help = "Get id of genre") String genreId) {
        var bookDtoList = bookService.getAllByGenreId(genreId);
        return getResponseFrom(bookDtoList);
    }

    @ShellMethod(value = "Update book", key = {"update-book", "ub"})
    public String update(@ShellOption(help = "Get id of the book") String bookId,
                         @ShellOption(help = "Get new name") String name,
                         @ShellOption(help = "Get new author_id", value = "--author") String authorId,
                         @ShellOption(help = "Get new genre_id", value = "--genre") String genreId) {
        var optionalBook = bookService.findById(bookId);
        if (optionalBook.isEmpty()) {
            return "Could not find the book id %s".formatted(bookId);
        }
        var optionalAuthor = authorService.findById(authorId);
        if (optionalAuthor.isEmpty()) {
            return "Could not find the author id %s".formatted(authorId);
        }
        var optionalGenre = genreService.findById(genreId);
        if (optionalGenre.isEmpty()) {
            return "Could not find the genre id %s".formatted(genreId);
        }
        var updatedBookDto = bookService.update(optionalBook.get(), name,
                optionalAuthor.get(), optionalGenre.get());
        return updatedBookDto == null
                ? "Failed to update book"
                : "Book id %s has been updated".formatted(updatedBookDto.id());
    }

    @ShellMethod(value = "Delete book", key = {"delete-book", "db"})
    public String deleteById(@ShellOption(help = "Get id of the book") String id) {
        bookService.deleteById(id);
        return "Book id %s was deleted".formatted(id);
    }

    private String getResponseFrom(List<BookDto> books) {
        return conversionService.convert(books, String.class);
    }
}
