package ru.pupov.homework09.controller;

import jakarta.servlet.http.HttpServletRequest;
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
import ru.pupov.homework09.dto.AuthorCreateDto;
import ru.pupov.homework09.entity.Author;
import ru.pupov.homework09.service.AuthorService;
import ru.pupov.homework09.service.BookService;

@Controller
@RequiredArgsConstructor
@Slf4j
public class AuthorController {

    private final AuthorService authorService;

    private final BookService bookService;

    @GetMapping("/author")
    public String listAuthorPage(Model model) {
        var authors = authorService.getAll();
        model.addAttribute("authors", authors);
        return "author_list";
    }

    @GetMapping("/author/books/{id}")
    public String listAuthorBooksPage(@PathVariable("id") long authorId, Model model) {
        var books = bookService.getAllByAuthorId(authorId);
        model.addAttribute("books", books);
        model.addAttribute("authorId", authorId);
        return "author_book_list";
    }

    @GetMapping("/author/create")
    public String createAuthorPage(Model model, HttpServletRequest request) {
        model.addAttribute("author", Author.builder().build());
        model.addAttribute("redirect", request.getHeader("Referer"));
        return "author_create";
    }

    @PostMapping("/author/create")
    public String createAuthor(@Validated @ModelAttribute("author") AuthorCreateDto authorCreateDto,
                               @ModelAttribute("redirect") String redirect,
                               BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("redirect", redirect);
            return "author_create";
        }
        authorService.create(authorCreateDto);
        if (redirect != null) {
            if (redirect.contains("book/create")) {
                return "redirect:/book/create";
            } else if (redirect.contains("book/edit")) {
                var afterHostStr = redirect.substring(redirect.lastIndexOf(":"));
                var redirectStr = afterHostStr.substring(afterHostStr.indexOf("/"));
                return "redirect:" + redirectStr;
            }
        }
        return "redirect:/author";
    }

    @GetMapping("/author/delete/{id}")
    public String deleteAuthor(@PathVariable long id, Model model) {
        try {
            authorService.deleteById(id);
        } catch (Exception e) {
            log.error(e.getMessage());
            model.addAttribute("exception", e.getMessage());
            var authors = authorService.getAll();
            model.addAttribute("authors", authors);
            return "author_list";
        }
        return "redirect:/author";
    }
}
