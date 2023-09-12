package ru.pupov.homework05.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.pupov.homework05.dao.GenreDao;
import ru.pupov.homework05.domain.Book;
import ru.pupov.homework05.domain.Genre;

import java.util.Arrays;
import java.util.List;

@ShellComponent
@RequiredArgsConstructor
public class GenreCommands {

    public static final String GET_ALL_TAG = "0";

    public static final String BOOK_IDS_DELIMITER = ",";

    private final GenreDao genreDao;

    private final ConversionService conversionService;

    @ShellMethod(value = "Create genre", key = {"create-genre", "cg"})
    public String create(@ShellOption(help = "Get genre's name") String name) {
        var id = genreDao.insert(Genre.builder()
                .name(name)
                .build());
        return "Genre id %d was created".formatted(id);
    }

    @ShellMethod(value = "Get genre(s)", key = {"read-genre", "rg"})
    public String read(
            @ShellOption(help = "Get id of the genre. '0' - for all genres", defaultValue = GET_ALL_TAG) Long id) {
        if (GET_ALL_TAG.equals(id.toString())) {
            var genres = genreDao.getAll();
            return getResponseFrom(genres);
        } else {
            var genre = genreDao.getById(id);
            return getResponseFrom(List.of(genre));
        }
    }

    @ShellMethod(value = "Update genre", key = {"update-genre", "ug"})
    public String update(
            @ShellOption(help = "Get id of the genre") Long id,
            @ShellOption(help = "Get new name") String name,
            @ShellOption(help = "Get books ids separated by comma, 'null' - do nothing", defaultValue = "null")
                    String bookIdsString) {
        var genre = genreDao.getById(id);
        if (genre != null) {
            genre.setName(name);
            if (bookIdsString != null && !bookIdsString.equals("null") && !bookIdsString.isBlank()) {
                genre.setBooks(Arrays.stream(bookIdsString.split(BOOK_IDS_DELIMITER))
                        .map(String::trim)
                        .map(Long::parseLong)
                        .map(bookId -> Book.builder().id(bookId).build())
                        .toList());
                genreDao.update(genre, true);
            } else {
                genreDao.update(genre, false);
            }
            return "Genre id %d has been updated".formatted(genre.getId());
        }
        return "Could not find the genre id %d".formatted(id);
    }

    @ShellMethod(value = "Delete genre", key = {"delete-genre", "dg"})
    public String deleteById(
            @ShellOption(help = "Get id of the genre") Long id) {
        genreDao.deleteById(id);
        return "Genre id %d was deleted".formatted(id);
    }

    private String getResponseFrom(List<Genre> genres) {
        return conversionService.convert(genres, String.class);
    }
}
