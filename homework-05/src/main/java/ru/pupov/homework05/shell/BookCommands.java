package ru.pupov.homework05.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.pupov.homework05.dao.BookDao;
import ru.pupov.homework05.domain.Author;
import ru.pupov.homework05.domain.Book;
import ru.pupov.homework05.domain.Genre;

import java.util.List;
import java.util.Objects;

@ShellComponent
@RequiredArgsConstructor
public class BookCommands {

    public static final String GET_ALL_TAG = "0";

    private final BookDao bookDao;

    private final ConversionService conversionService;

    @ShellMethod(value = "Create book", key = {"create-book", "cb"})
    public String create(@ShellOption(help = "Get book's name") String name,
                         @ShellOption(help = "Get author id") Long authorId,
                         @ShellOption(help = "Get genre id") Long genreId) {
        if (authorId == null || genreId == null) {
            return "Please, enter author id and genre id";
        }
        var id = bookDao.insert(Book.builder()
                .name(name)
                .author(Author.builder()
                        .id(authorId)
                        .build())
                .genre(Genre.builder()
                        .id(genreId)
                        .build())
                .build());
        return "Book id %d was created".formatted(id);
    }

    @ShellMethod(value = "Get book(s)", key = {"read-book", "rb"})
    public String read(
            @ShellOption(help = "Get id of the book. '0' - for all books", defaultValue = GET_ALL_TAG) Long id) {
        if (GET_ALL_TAG.equals(id.toString())) {
            var books = bookDao.getAll();
            return getResponseFrom(books);
        } else {
            var book = bookDao.getById(id);
            return getResponseFrom(List.of(book));
        }
    }

    @ShellMethod(value = "Update book", key = {"update-book", "ub"})
    public String update(
            @ShellOption(help = "Get id of the book") Long id,
            @ShellOption(help = "Get new name") String name,
            @ShellOption(help = "Get new author_id", value = "--author") Long authorId,
            @ShellOption(help = "Get new genre_id", value = "--genre") Long genreId) {
        var book = bookDao.getById(id);
        if (book != null) {
            if (name != null && !name.isBlank()) {
                book.setName(name);
            }
            if (authorId != null && !Objects.equals(authorId, book.getAuthor().getId())) {
                book.setAuthor(Author.builder()
                        .id(authorId)
                        .build());
            }
            if (genreId != null && !Objects.equals(genreId, book.getGenre().getId())) {
                book.setGenre(Genre.builder()
                        .id(genreId)
                        .build());
            }
            bookDao.update(book);
            return "Book id %d has been updated".formatted(book.getId());
        }
        return "Could not find the book id %d".formatted(id);
    }

    @ShellMethod(value = "Delete book", key = {"delete-book", "db"})
    public String deleteById(
            @ShellOption(help = "Get id of the book") Long id) {
        bookDao.deleteById(id);
        return "Book id %d was deleted".formatted(id);
    }

    private String getResponseFrom(List<Book> books) {
        return conversionService.convert(books, String.class);
    }
}
