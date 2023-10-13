package ru.pupov.homework06.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.pupov.homework06.dto.GenreDto;
import ru.pupov.homework06.entity.Genre;
import ru.pupov.homework06.service.GenreService;

import java.util.Collections;
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
        var genre = new Genre(null, name, Collections.emptyList());
        var savedGenre = genreService.save(genre);
        return savedGenre == null
                ? "Failed to create genre"
                : "Genre id %d was created".formatted(savedGenre.id());
    }

    @ShellMethod(value = "Get genre by id", key = {"read-genre", "rg"})
    public String readById(@ShellOption(help = "Get id of the genre") Long id) {
        return genreService.getById(id)
                .map(genreDto -> getResponseFrom(List.of(genreDto)))
                .orElse("Could not find the genre id %d".formatted(id));
    }

    @ShellMethod(value = "Get genres", key = {"read-all-genres", "rgs"})
    public String readAll() {
        var genreDtoList = genreService.getAll();
        return getResponseFrom(genreDtoList);
    }

    @ShellMethod(value = "Update genre", key = {"update-genre", "ug"})
    public String update(@ShellOption(help = "Get id of the genre") Long id,
                         @ShellOption(help = "Get new name") String name) {
        var optionalGenre = genreService.findById(id);
        if (optionalGenre.isEmpty()) {
            return "Could not find the genre id %d".formatted(id);
        }
        var updatedGenreDto = genreService.update(optionalGenre.get(), name);
        return updatedGenreDto == null
                ? "Failed to create genre"
                : "Genre id %d has been updated".formatted(updatedGenreDto.id());
    }

    @ShellMethod(value = "Delete genre", key = {"delete-genre", "dg"})
    public String deleteById(@ShellOption(help = "Get id of the genre") Long id) {
        var result = genreService.deleteById(id);
        return result ? "Genre id %d was deleted".formatted(id) : "Could not find genre id %s".formatted(id);
    }

    private String getResponseFrom(List<GenreDto> genres) {
        return conversionService.convert(genres, String.class);
    }
}
