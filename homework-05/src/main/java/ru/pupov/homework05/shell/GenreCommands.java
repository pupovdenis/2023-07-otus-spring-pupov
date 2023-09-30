package ru.pupov.homework05.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.pupov.homework05.domain.Genre;
import ru.pupov.homework05.service.GenreService;

import java.util.List;

@ShellComponent
@RequiredArgsConstructor
public class GenreCommands {

    private final GenreService genreService;

    private final ConversionService conversionService;

    @ShellMethod(value = "Create genre", key = {"create-genre", "cg"})
    public String create(@ShellOption(help = "Get genre's name") String name) {
        var id = genreService.insert(Genre.builder()
                .name(name)
                .build());
        return id == null ? "Failed to create genre" : "Genre id %d was created".formatted(id);
    }

    @ShellMethod(value = "Get genre by id", key = {"read-genre", "rg"})
    public String readById(@ShellOption(help = "Get id of the genre") Long id) {
        var genre = genreService.getById(id);
        return genre == null ? "null" : getResponseFrom(List.of(genre));
    }

    @ShellMethod(value = "Get genres", key = {"read-all-genres", "rgs"})
    public String readAll() {
        var genres = genreService.getAll();
        return getResponseFrom(genres);
    }

    @ShellMethod(value = "Update genre", key = {"update-genre", "ug"})
    public String update(
            @ShellOption(help = "Get id of the genre") Long id,
            @ShellOption(help = "Get new name") String name) {
        var genre = genreService.getById(id);
        if (genre != null) {
            genre.setName(name);
            genreService.update(genre);
            return "Genre id %d has been updated".formatted(genre.getId());
        }
        return "Could not find the genre id %d".formatted(id);
    }

    @ShellMethod(value = "Delete genre", key = {"delete-genre", "dg"})
    public String deleteById(@ShellOption(help = "Get id of the genre") Long id) {
        var result = genreService.deleteById(id);
        return result ? "Genre id %d was deleted".formatted(id) : "Could not find genre id %s".formatted(id);
    }

    private String getResponseFrom(List<Genre> genres) {
        return conversionService.convert(genres, String.class);
    }
}
