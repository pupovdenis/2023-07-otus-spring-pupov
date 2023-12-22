package ru.pupov.homework08.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.pupov.homework08.dto.GenreDto;
import ru.pupov.homework08.model.Genre;
import ru.pupov.homework08.service.GenreService;

import java.util.List;

@ShellComponent
@RequiredArgsConstructor
public class GenreCommands {

    private final GenreService genreService;

    private final ConversionService conversionService;

    @ShellMethod(value = "Create genre", key = {"create-genre", "cg"})
    public String create(@ShellOption(help = "Get genre's name") String name) {
        if (name.isBlank()) {
            return "Please, enter name";
        }
        var genre = new Genre(name);
        var savedGenre = genreService.save(genre);
        return savedGenre == null
                ? "Failed to create genre"
                : "Genre id %s was created".formatted(savedGenre.id());
    }

    @ShellMethod(value = "Get genre by id", key = {"read-genre", "rg"})
    public String readById(@ShellOption(help = "Get id of the genre") String id) {
        return genreService.getById(id)
                .map(genreDto -> getResponseFrom(List.of(genreDto)))
                .orElse("Could not find the genre id %s".formatted(id));
    }

    @ShellMethod(value = "Get genres", key = {"read-all-genres", "rgs"})
    public String readAll() {
        var genreDtoList = genreService.getAll();
        return getResponseFrom(genreDtoList);
    }

    @ShellMethod(value = "Delete genre", key = {"delete-genre", "dg"})
    public String deleteById(@ShellOption(help = "Get id of the genre") String id) {
        genreService.deleteById(id);
        return "Genre id %s was deleted".formatted(id);
    }

    private String getResponseFrom(List<GenreDto> genres) {
        return conversionService.convert(genres, String.class);
    }
}
