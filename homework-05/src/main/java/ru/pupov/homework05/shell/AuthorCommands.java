package ru.pupov.homework05.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.pupov.homework05.dao.AuthorDao;
import ru.pupov.homework05.domain.Author;

import java.util.List;

@ShellComponent
@RequiredArgsConstructor
public class AuthorCommands {

    public static final String GET_ALL_TAG = "0";

    private final AuthorDao authorDao;

    private final ConversionService conversionService;

    @ShellMethod(value = "Create author", key = {"create-author", "ca"})
    public String create(@ShellOption(value = "first-name", help = "Get author's first name") String firstName,
                         @ShellOption(value = "last-name", help = "Get author's last name") String lastName) {
        var id = authorDao.insert(Author.builder()
                .firstName(firstName)
                .lastName(lastName)
                .build());
        return "Author id %d was created".formatted(id);
    }

    @ShellMethod(value = "Get author(s)", key = {"read-author", "ra"})
    public String read(
            @ShellOption(help = "Get id of the author. '0' - for all authors", defaultValue = GET_ALL_TAG) Long id) {
        if (GET_ALL_TAG.equals(id.toString())) {
            var authors = authorDao.getAll();
            return getResponseFrom(authors);
        } else {
            var author = authorDao.getById(id);
            return author == null ? "null" : getResponseFrom(List.of(author));
        }
    }

    @ShellMethod(value = "Update author", key = {"update-author", "ua"})
    public String update(
            @ShellOption(help = "Get id of the author") Long id,
            @ShellOption(help = "Get new first name") String firstName,
            @ShellOption(help = "Get new last name") String lastName,
            @ShellOption(help = "Get books ids separated by comma, 'null' - do nothing", defaultValue = "null")
                    String bookIdsString) {
        var author = authorDao.getById(id);
        if (author != null) {
            author.setFirstName(firstName);
            author.setLastName(lastName);
            authorDao.update(author, bookIdsString);
            return "Author id %d has been updated".formatted(author.getId());
        }
        return "Could not find the author id %d".formatted(id);
    }

    @ShellMethod(value = "Delete author", key = {"delete-author", "da"})
    public String deleteById(
            @ShellOption(help = "Get id of the Author") Long id) {
        var result = authorDao.deleteById(id);
        return result ? "Author id %d was deleted".formatted(id) : "Could not find author id %s".formatted(id);
    }

    private String getResponseFrom(List<Author> authors) {
        return conversionService.convert(authors, String.class);
    }
}
