package ru.pupov.homework08.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.pupov.homework08.dto.CommentDto;
import ru.pupov.homework08.service.BookService;

import java.util.List;

@ShellComponent
@RequiredArgsConstructor
public class CommentCommands {

    private final BookService bookService;

    private final ConversionService conversionService;

    @ShellMethod(value = "Add comment", key = {"add-comment", "ac"})
    public String addComment(@ShellOption(help = "Get comment's text", value = "--text") String text,
                             @ShellOption(help = "Get book id", value = "--book") String bookId) {
        if (text.isBlank()) {
            return "Please, enter comment's text";
        }
        if (bookId == null) {
            return "Please, enter book id";
        }
        var result = bookService.addComment(bookId, text);
        return result
                ? "Comment '%s' was added to book id %s".formatted(text, bookId)
                : "Failed to create comment";
    }

    @ShellMethod(value = "Get comments by book id", key = {"read-comments-by-book-id", "rcb"})
    public String readAllByBookId(@ShellOption(help = "Get id of the book") String bookId) {
        if (bookId == null || bookId.isBlank()) {
            return "Please, enter book id";
        }
        var commentDtoList = bookService.getCommentsByBookId(bookId);
        return getResponseFrom(commentDtoList);
    }

    @ShellMethod(value = "Update comment", key = {"update-comment", "uc"})
    public String update(@ShellOption(help = "Get id of the book", value = "--book") String bookId,
                         @ShellOption(help = "Get id of the comment", value = "--comment") String commentId,
                         @ShellOption(help = "Get new comment's text", value = "--text") String text) {
        if (bookId == null || bookId.isBlank()) {
            return "Please, enter book id";
        }
        if (commentId == null || commentId.isBlank()) {
            return "Please, enter comment id";
        }
        var result = bookService.updateCommentById(bookId, commentId, text);
        return result
                ? "Comment id %s has been updated".formatted(commentId)
                : "Failed to create comment";
    }

    @ShellMethod(value = "Delete comment", key = {"delete-comment", "dc"})
    public String deleteById(@ShellOption(help = "Get id of the book", value = "--book") String bookId,
                             @ShellOption(help = "Get id of the comment", value = "--comment") String commentId) {
        if (bookId == null || bookId.isBlank()) {
            return "Please, enter book id";
        }
        if (commentId == null || commentId.isBlank()) {
            return "Please, enter comment id";
        }
        var result = bookService.deleteCommentByIds(bookId, List.of(commentId));
        return result
                ? "Comment %s has been removed from book id %s".formatted(commentId, bookId)
                : "Failed to delete comment";
    }

    private String getResponseFrom(List<CommentDto> comments) {
        return conversionService.convert(comments, String.class);
    }
}
