package ru.pupov.homework09.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.pupov.homework09.dto.GenreCreateDto;
import ru.pupov.homework09.service.BookService;
import ru.pupov.homework09.service.GenreService;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(GenreController.class)
@DisplayName("genre контроллер должен")
class GenreControllerTest {

    public static final Long FIRST_ID = 1L;
    public static final String REDIRECT_URL = "/test";
    public static final String GENRE_NAME = "test";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GenreService genreService;

    @MockBean
    private BookService bookService;

    @Test
    @DisplayName("корректно вывести главную страницу /genre")
    void getListGenrePage_doGetRequest_pageWithGenres() throws Exception {
        mockMvc.perform(get("/genre"))
                .andExpect(status().isOk())
                .andExpect(view().name("genre_list"))
                .andExpect(model().attributeExists("genres"));
    }

    @Test
    @DisplayName("корректно вывести страницу с книгами определенного жанра")
    void getGenreBooksListPage_doGetRequest_pageWithGenreBooks() throws Exception {
        mockMvc.perform(get("/genre/books/{id}", FIRST_ID)
                        .param("name", GENRE_NAME))
                .andExpect(status().isOk())
                .andExpect(view().name("genre_book_list"))
                .andExpect(model().attributeExists("books"))
                .andExpect(model().attributeExists("genreName"));
        verify(bookService, times(1)).getAllByGenreId(FIRST_ID);
    }

    @Test
    @DisplayName("корректно вывести страницу создания жанра /genre/create")
    void createGenrePage_doGetRequest_pageWithNeedAttributes() throws Exception {
        mockMvc.perform(get("/genre/create")
                        .header("Referer", REDIRECT_URL))
                .andExpect(status().isOk())
                .andExpect(view().name("genre_create"))
                .andExpect(model().attributeExists("genre"))
                .andExpect(model().attributeExists("redirect"));
    }

    @Test
    @DisplayName("корректно создать жанр и переводить на нужную страницу /genre")
    void createGenre_doSubmitForm_genrePage() throws Exception {
        var genreCreateDto = GenreCreateDto.builder()
                .name(GENRE_NAME)
                .build();

        mockMvc.perform(post("/genre/create")
                        .flashAttr("genre", genreCreateDto)
                        .param("redirect", REDIRECT_URL))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/genre"));
        verify(genreService, times(1)).create(genreCreateDto);
    }

    @Test
    @DisplayName("корректно удалять жанр и переводить на нужную страницу /genre")
    void deleteBook_doGetRequest_redirectToGenrePage() throws Exception {
        mockMvc.perform(get("/genre/delete/{id}", FIRST_ID))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/genre"));
        verify(genreService, times(1)).deleteById(FIRST_ID);
    }
}