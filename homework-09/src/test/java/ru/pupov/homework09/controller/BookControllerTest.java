package ru.pupov.homework09.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.pupov.homework09.dto.BookCreateUpdateDto;
import ru.pupov.homework09.dto.BookDto;
import ru.pupov.homework09.service.AuthorService;
import ru.pupov.homework09.service.BookService;
import ru.pupov.homework09.service.GenreService;

import java.util.Optional;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(BookController.class)
@DisplayName("book контроллер должен")
class BookControllerTest {

    public static final Long FIRST_ID = 1L;
    public static final String BOOK_NAME = "book name";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @MockBean
    private AuthorService authorService;

    @MockBean
    private GenreService genreService;

    @Test
    @DisplayName("корректно вывести главную страницу /book")
    void getListBookPage_doGetRequest_pageWithBooksAttribute() throws Exception {
        mockMvc.perform(get("/book"))
                .andExpect(status().isOk())
                .andExpect(view().name("book_list"))
                .andExpect(model().attributeExists("books"));
    }

    @Test
    @DisplayName("корректно вывести страницу создания книги /book/create")
    void createBookPage_doGetRequest_pageWithNeedAttributes() throws Exception {
        mockMvc.perform(get("/book/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("book_create"))
                .andExpect(model().attributeExists("book"))
                .andExpect(model().attributeExists("authors"))
                .andExpect(model().attributeExists("genres"));
    }

    @Test
    @DisplayName("корректно создать книгу и перевести на главную страницу /book")
    void createBook_doSubmitForm_pageWithUpdatedBook() throws Exception {
        mockMvc.perform(post("/book/create")
                        .param("name", BOOK_NAME)
                        .param("authorId", "1")
                        .param("genreId", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/book"));
    }

    @Test
    @DisplayName("вернуть на страницу book_create при попытке создать книгу с ошибочными данными")
    void createBook_doSubmitFormWithTwoBindingResultErrors_needPage() throws Exception {
        mockMvc.perform(post("/book/create")
                        .flashAttr("book", BookCreateUpdateDto.builder()
                                .authorId(FIRST_ID)
                                .build()))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("book", "name"))
                .andExpect(model().attributeHasFieldErrors("book", "genreId"))
                .andExpect(status().isOk())
                .andExpect(view().name("book_create"))
                .andExpect(model().attributeExists("book"))
                .andExpect(model().attributeExists("authors"))
                .andExpect(model().attributeExists("genres"));
    }

    @Test
    @DisplayName("корректно вывести страницу редактирования книги /book/edit")
    void editBookPage_doGetRequest_pageWithNeedAttributes() throws Exception {
        when(bookService.getById(FIRST_ID)).thenReturn(Optional.of(BookDto.builder().id(FIRST_ID).build()));

        mockMvc.perform(get("/book/edit")
                        .param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("book_edit"))
                .andExpect(model().attributeExists("book"))
                .andExpect(model().attributeExists("authors"))
                .andExpect(model().attributeExists("genres"));
    }

    @Test
    @DisplayName("корректно перенаправить на страницу /book после обновления книги")
    void updateBook_doSubmitForm_pageWithBooksAttribute() throws Exception {
        mockMvc.perform(post("/book/edit")
                        .param("id", FIRST_ID.toString())
                        .flashAttr("book", BookCreateUpdateDto.builder()
                                .name(BOOK_NAME)
                                .authorId(FIRST_ID)
                                .genreId(FIRST_ID)
                                .build()))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/book"));
    }

    @Test
    @DisplayName("корректно перенаправить на страницу /book после удаления книги")
    void deleteBook_doGetRequest_pageWithBooksAttribute() throws Exception {
        mockMvc.perform(get("/book/delete/{id}", FIRST_ID))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/book"));
        verify(bookService, times(1)).deleteById(FIRST_ID);
    }
}