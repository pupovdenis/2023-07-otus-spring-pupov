package ru.pupov.homework09.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.pupov.homework09.dto.AuthorCreateDto;
import ru.pupov.homework09.service.AuthorService;
import ru.pupov.homework09.service.BookService;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(AuthorController.class)
@DisplayName("author контроллер должен")
class AuthorControllerTest {

    public static final Long FIRST_ID = 1L;
    public static final String REDIRECT_URL = "/test";
    public static final String AUTHOR_FIRSTNAME = "author_firstname";
    public static final String AUTHOR_LASTNAME = "author_lastname";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthorService authorService;

    @MockBean
    private BookService bookService;

    @Test
    @DisplayName("корректно вывести главную страницу /author")
    void getListAuthorPage_doGetRequest_pageWithAuthors() throws Exception {
        mockMvc.perform(get("/author"))
                .andExpect(status().isOk())
                .andExpect(view().name("author_list"))
                .andExpect(model().attributeExists("authors"));
    }

    @Test
    @DisplayName("корректно вывести страницу с книгами определенного автора")
    void getAuthorBooksListPage_doGetRequest_pageWithAuthorBooks() throws Exception {
        mockMvc.perform(get("/author/books/{id}", FIRST_ID))
                .andExpect(status().isOk())
                .andExpect(view().name("author_book_list"))
                .andExpect(model().attributeExists("books"))
                .andExpect(model().attributeExists("authorId"));
        verify(bookService, times(1)).getAllByAuthorId(FIRST_ID);
    }

    @Test
    @DisplayName("корректно вывести страницу создания автора /author/create")
    void createAuthorPage_doGetRequest_pageWithNeedAttributes() throws Exception {
        mockMvc.perform(get("/author/create")
                        .header("Referer", REDIRECT_URL))
                .andExpect(status().isOk())
                .andExpect(view().name("author_create"))
                .andExpect(model().attributeExists("author"))
                .andExpect(model().attributeExists("redirect"));
    }

    @Test
    @DisplayName("корректно создать автора и переводить на нужную страницу /author")
    void createAuthor_doSubmitForm_authorPage() throws Exception {
        var authorCreateDto = AuthorCreateDto.builder()
                .firstname(AUTHOR_FIRSTNAME)
                .lastname(AUTHOR_LASTNAME).build();

        mockMvc.perform(post("/author/create")
                        .flashAttr("author", authorCreateDto)
                        .param("redirect", REDIRECT_URL))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/author"));
        verify(authorService, times(1)).create(authorCreateDto);
    }

    @Test
    @DisplayName("корректно удалять автора и переводить на нужную страницу /author")
    void deleteBook_doGetRequest_redirectToAuthorPage() throws Exception {
        mockMvc.perform(get("/author/delete/{id}", FIRST_ID))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/author"));
        verify(authorService, times(1)).deleteById(FIRST_ID);
    }
}