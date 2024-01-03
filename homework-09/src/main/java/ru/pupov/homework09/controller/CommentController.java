package ru.pupov.homework09.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.pupov.homework09.dto.CommentDto;
import ru.pupov.homework09.service.CommentService;

@Controller
@RequiredArgsConstructor
@Slf4j
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/comments/{bookId}")
    public String listCommentPage(@ModelAttribute("addComment") CommentDto commentDto,
                                  @PathVariable long bookId, Model model) {
        var comments = commentService.getAllByBookId(bookId);
        model.addAttribute("comments", comments);
        model.addAttribute("bookId", bookId);
        return "comment_list";
    }

    @PostMapping("/comments/{bookId}/add")
    public String addComment(@Validated @ModelAttribute("addComment") CommentDto commentDto, BindingResult bindingResult,
                             @PathVariable("bookId") long bookId, RedirectAttributes redirectAttrs, Model model) {
        if (bindingResult.hasErrors()) {
            var comments = commentService.getAllByBookId(bookId);
            model.addAttribute("comments", comments);
            model.addAttribute("bookId", bookId);
            return "comment_list";
        }
        commentService.add(commentDto);
        redirectAttrs.addFlashAttribute("addComment", CommentDto.builder().build());
        return "redirect:/comments/" + bookId;
    }

    @GetMapping("/comments/{bookId}/edit/{commentId}")
    public String editCommentPage(@PathVariable("bookId") long bookId,
                                  @PathVariable("commentId") long commentId,
                                  Model model) {
        var comments = commentService.getAllByBookId(bookId);
        var editComment = comments.stream()
                .filter(commentDto -> commentDto.id().equals(commentId))
                .findFirst()
                .orElse(null);
        model.addAttribute("comments", comments);
        model.addAttribute("bookId", bookId);
        model.addAttribute("editCommentId", commentId);
        model.addAttribute("editComment", editComment);
        return "comment_list";
    }

    @PostMapping("/comments/{bookId}/edit/{commentId}")
    public String updateComment(@ModelAttribute("editComment") CommentDto commentDto,
                                @PathVariable(name = "bookId") long bookId,
                                @PathVariable(name = "commentId") long commentId,
                                Model model) {
        var updatedComment = commentService.update(commentId, commentDto.text());
        var comments = commentService.getAllByBookId(bookId);
        model.addAttribute("comments", comments);
        model.addAttribute("bookId", bookId);
        model.addAttribute("editCommentId", commentId);
        model.addAttribute("editComment", updatedComment);
        return "comment_list";
    }

    @GetMapping("/comments/{bookId}/delete/{commentId}")
    public String deleteComment(@PathVariable("bookId") long bookId,
                                @PathVariable("commentId") long commentId, Model model) {
        try {
            commentService.deleteById(commentId);
        } catch (Exception e) {
            log.error(e.getMessage());
            model.addAttribute("exception", e.getCause().getMessage());
            var comments = commentService.getAllByBookId(commentId);
            model.addAttribute("comments", comments);
            model.addAttribute("bookId", commentId);
            return "comment_list";
        }
        return "redirect:/comments/" + bookId;
    }
}
