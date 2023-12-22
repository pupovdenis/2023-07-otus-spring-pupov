package ru.pupov.homework08.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.pupov.homework08.dto.AuthorDto;
import ru.pupov.homework08.model.Author;
import ru.pupov.homework08.service.AuthorService;

import java.util.List;

@ShellComponent
@RequiredArgsConstructor
public class AuthorCommands {

    private final AuthorService authorService;

    private final ConversionService conversionService;

    @ShellMethod(value = "Create author", key = {"create-author", "ca"})
    public String create(@ShellOption(help = "Get author's first name", value = "--firstname") String firstname,
                         @ShellOption(help = "Get author's last name", value = "--lastname") String lastname) {
        if (firstname.isBlank()) {
            return "Please, enter firstname";
        }
        if (lastname.isBlank()) {
            return "Please, enter lastname";
        }
        var author = new Author(firstname, lastname);
        var savedAuthorDto = authorService.save(author);
        return savedAuthorDto == null
                ? "Failed to create author"
                : "Author id %s was created".formatted(savedAuthorDto.id());
    }

    @ShellMethod(value = "Get author by id", key = {"read-author", "ra"})
    public String readById(@ShellOption(help = "Get id of the author") String authorId) {
        return authorService.getById(authorId)
                .map(comment -> getResponseFrom(List.of(comment)))
                .orElse("Could not find the author id %s".formatted(authorId));
    }

    @ShellMethod(value = "Get authors", key = {"read-all-authors", "ras"})
    public String readAll() {
        var authors = authorService.getAll();
        return getResponseFrom(authors);
    }

    @ShellMethod(value = "Delete author", key = {"delete-author", "da"})
    public String deleteById(@ShellOption(help = "Get id of the Author") String authorId) {
        authorService.deleteById(authorId);
        return "Author id %s was deleted".formatted(authorId);
    }

    private String getResponseFrom(List<AuthorDto> authors) {
        return conversionService.convert(authors, String.class);
    }
}
