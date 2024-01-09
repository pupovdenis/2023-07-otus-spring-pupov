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
import org.springframework.web.bind.annotation.RequestParam;
import ru.pupov.homework09.dto.GenreCreateDto;
import ru.pupov.homework09.entity.Genre;
import ru.pupov.homework09.service.BookService;
import ru.pupov.homework09.service.GenreService;

@Controller
@RequiredArgsConstructor
@Slf4j
public class GenreController {

    private final GenreService genreService;

    private final BookService bookService;

    @GetMapping("/genre")
    public String listGenrePage(Model model) {
        var genres = genreService.getAll();
        model.addAttribute("genres", genres);
        return "genre_list";
    }

    @GetMapping("/genre/books/{id}")
    public String listGenreBooksPage(@PathVariable("id") long genreId,
                                     @RequestParam("name") String genreName, Model model) {
        var books = bookService.getAllByGenreId(genreId);
        model.addAttribute("books", books);
        model.addAttribute("genreName", genreName);
        return "genre_book_list";
    }

    @GetMapping("/genre/create")
    public String createGenrePage(Model model, HttpServletRequest request) {
        model.addAttribute("genre", Genre.builder().build());
        model.addAttribute("redirect", request.getHeader("Referer"));
        return "genre_create";
    }

    @PostMapping("/genre/create")
    public String createGenre(@Validated @ModelAttribute("genre") GenreCreateDto genreCreateDto,
                              @ModelAttribute("redirect") String redirect,
                              BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("redirect", redirect);
            return "genre_create";
        }
        genreService.create(genreCreateDto);
        if (redirect != null) {
            if (redirect.contains("book/create")) {
                return "redirect:/book/create";
            } else if (redirect.contains("book/edit")) {
                var afterHostStr = redirect.substring(redirect.lastIndexOf(":"));
                var redirectStr = afterHostStr.substring(afterHostStr.indexOf("/"));
                return "redirect:" + redirectStr;
            }
        }
        return "redirect:/genre";
    }

    @GetMapping("/genre/delete/{id}")
    public String deleteGenre(@PathVariable long id, Model model) {
        try {
            genreService.deleteById(id);
        } catch (Exception e) {
            log.error(e.getMessage());
            model.addAttribute("exception", e.getCause().getMessage());
            var genres = genreService.getAll();
            model.addAttribute("genres", genres);
            return "genre_list";
        }
        return "redirect:/genre";
    }
}
