package ru.pupov.homework07.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.pupov.homework07.dto.CommentDto;
import ru.pupov.homework07.entity.Comment;
import ru.pupov.homework07.service.BookService;
import ru.pupov.homework07.service.CommentService;

import java.util.List;

@ShellComponent
@RequiredArgsConstructor
public class CommentCommands {

    private final CommentService commentService;

    private final BookService bookService;

    private final ConversionService conversionService;

    @ShellMethod(value = "Create comment", key = {"create-comment", "cc"})
    public String create(@ShellOption(help = "Get comment's text") String text,
                         @ShellOption(help = "Get book id") Long bookId) {
        if (text.isBlank()) {
            return "Please, enter comment's text";
        }
        if (bookId == null) {
            return "Please, enter book id";
        }
        var optionalBook = bookService.findById(bookId);
        if (optionalBook.isEmpty()) {
            return "Could not find book id %d".formatted(bookId);
        }
        var comment = new Comment(null, text, optionalBook.get());
        var savedCommentDto = commentService.save(comment);
        return savedCommentDto == null
                ? "Failed to create comment"
                : "Comment id %d was created".formatted(savedCommentDto.id());
    }

    @ShellMethod(value = "Get comment by id", key = {"read-comment", "rc"})
    private String readById(@ShellOption(help = "Get id of the comment") final Long id) {
        return commentService.getById(id)
                .map(commentDto -> getResponseFrom(List.of(commentDto)))
                .orElse("Could not find the comment id %d".formatted(id));
    }

    @ShellMethod(value = "Get comments by book id", key = {"read-comments-by-book-id", "rcb"})
    public String readAllByBookId(@ShellOption(help = "Get id of the book") Long bookId) {
        var commentDtoList = commentService.getAllByBookId(bookId);
        return getResponseFrom(commentDtoList);
    }

    @ShellMethod(value = "Update comment", key = {"update-comment", "uc"})
    public String update(@ShellOption(help = "Get id of the comment") Long id,
                         @ShellOption(help = "Get new comment's text") String text) {
        var optionalComment = commentService.findById(id);
        if (optionalComment.isEmpty()) {
            return "Could not find the comment id %d".formatted(id);
        }
        var updatedCommentDto = commentService.update(optionalComment.get(), text);
        return updatedCommentDto == null
                ? "Failed to create comment"
                : "Comment id %d has been updated".formatted(updatedCommentDto.id());
    }

    @ShellMethod(value = "Delete comment", key = {"delete-comment", "dc"})
    public String deleteById(@ShellOption(help = "Get id of the comment") Long id) {
        commentService.deleteById(id);
        return "Comment id %d was deleted".formatted(id);
    }

    private String getResponseFrom(List<CommentDto> comments) {
        return conversionService.convert(comments, String.class);
    }
}
