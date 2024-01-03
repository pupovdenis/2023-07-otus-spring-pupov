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
import org.springframework.web.bind.annotation.RequestParam;
import ru.pupov.homework09.dto.BookCreateUpdateDto;
import ru.pupov.homework09.dto.BookUpdateDto;
import ru.pupov.homework09.service.AuthorService;
import ru.pupov.homework09.service.BookService;
import ru.pupov.homework09.service.GenreService;

@Controller
@RequiredArgsConstructor
@Slf4j
public class BookController {

    private final BookService bookService;

    private final AuthorService authorService;

    private final GenreService genreService;

    @GetMapping("/book")
    public String listBookPage(Model model) {
        var books = bookService.getAll();
        model.addAttribute("books", books);
        return "book_list";
    }

    @GetMapping("/book/create")
    public String createBookPage(Model model) {
        var authorDtos = authorService.getAll();
        var genreDtos = genreService.getAll();
        model.addAttribute("book", BookUpdateDto.builder().build());
        model.addAttribute("authors", authorDtos);
        model.addAttribute("genres", genreDtos);
        return "book_create";
    }

    @PostMapping("/book/create")
    public String createBook(@Validated @ModelAttribute("book") BookCreateUpdateDto bookCreateDto,
                             BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("book", bookCreateDto);
            model.addAttribute("authors", authorService.getAll());
            model.addAttribute("genres", genreService.getAll());
            return "book_create";
        }
        var bookDto = bookService.create(bookCreateDto);
        model.addAttribute("book", bookDto);

        return "redirect:/book";
    }

    @GetMapping("/book/edit")
    public String editBookPage(@RequestParam("id") long id, Model model) {
        var book = bookService.getById(id).orElse(null);
        var authorDtos = authorService.getAll();
        var genreDtos = genreService.getAll();
        model.addAttribute("book", book);
        model.addAttribute("authors", authorDtos);
        model.addAttribute("genres", genreDtos);
        return "book_edit";
    }

    @PostMapping("/book/edit")
    public String updateBook(@RequestParam(name = "id") Long id,
                             @Validated @ModelAttribute("book") BookCreateUpdateDto bookDto,
                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "book_edit";
        }
        bookService.update(
                id,
                bookDto.name(),
                bookDto.authorId(),
                bookDto.genreId()
        );
        return "redirect:/book";
    }

    @GetMapping("/book/delete/{id}")
    public String deleteBook(@PathVariable long id, Model model) {
        try {
            bookService.deleteById(id);
        } catch (Exception e) {
            log.error(e.getMessage());
            model.addAttribute("exception", e.getCause().getMessage());
            var books = bookService.getAll();
            model.addAttribute("books", books);
            return "book_list";
        }
        return "redirect:/book";
    }
}
