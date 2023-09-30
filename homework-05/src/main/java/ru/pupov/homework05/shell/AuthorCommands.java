package ru.pupov.homework05.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.pupov.homework05.domain.Author;
import ru.pupov.homework05.service.AuthorService;

import java.util.List;

@ShellComponent
@RequiredArgsConstructor
public class AuthorCommands {

    private final AuthorService authorService;

    private final ConversionService conversionService;

    @ShellMethod(value = "Create author", key = {"create-author", "ca"})
    public String create(@ShellOption(value = "first-name", help = "Get author's first name") String firstName,
                         @ShellOption(value = "last-name", help = "Get author's last name") String lastName) {
        var id = authorService.insert(Author.builder()
                .firstName(firstName)
                .lastName(lastName)
                .build());
        return id == null ? "Failed to create author" : "Author id %d was created".formatted(id);
    }

    @ShellMethod(value = "Get author by id", key = {"read-author", "ra"})
    public String readById(@ShellOption(help = "Get id of the author") Long id) {
        var author = authorService.getById(id);
        return author == null ? "null" : getResponseFrom(List.of(author));
    }

    @ShellMethod(value = "Get authors", key = {"read-all-authors", "ras"})
    public String readAll() {
        var authors = authorService.getAll();
        return getResponseFrom(authors);
    }

    @ShellMethod(value = "Update author", key = {"update-author", "ua"})
    public String update(
            @ShellOption(help = "Get id of the author") Long id,
            @ShellOption(help = "Get new first name") String firstName,
            @ShellOption(help = "Get new last name") String lastName) {
        var author = authorService.getById(id);
        if (author != null) {
            author.setFirstName(firstName);
            author.setLastName(lastName);
            authorService.update(author);
            return "Author id %d has been updated".formatted(author.getId());
        }
        return "Could not find the author id %d".formatted(id);
    }

    @ShellMethod(value = "Delete author", key = {"delete-author", "da"})
    public String deleteById(
            @ShellOption(help = "Get id of the Author") Long id) {
        var result = authorService.deleteById(id);
        return result ? "Author id %d was deleted".formatted(id) : "Could not find author id %s".formatted(id);
    }

    private String getResponseFrom(List<Author> authors) {
        return conversionService.convert(authors, String.class);
    }
}
