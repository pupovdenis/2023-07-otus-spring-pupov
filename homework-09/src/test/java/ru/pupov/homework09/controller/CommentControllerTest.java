package ru.pupov.homework09.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.pupov.homework09.dto.BookDto;
import ru.pupov.homework09.dto.CommentDto;
import ru.pupov.homework09.service.CommentService;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(CommentController.class)
@DisplayName("comment контроллер должен")
class CommentControllerTest {

    public static final Long FIRST_ID = 1L;
    public static final Long SECOND_ID = 2L;
    public static final String COMMENT_TEXT = "comment text";
    public static final String REDIRECT_URL = "/test";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentService commentService;

    @Test
    @DisplayName("корректно вывести главную страницу /comments")
    void getListCommentPage_doGetRequest_pageWithBookComments() throws Exception {
        mockMvc.perform(get("/comments/{bookId}", FIRST_ID)
                        .param("bookId", FIRST_ID.toString()))
                .andExpect(status().isOk())
                .andExpect(view().name("comment_list"))
                .andExpect(model().attributeExists("bookId"))
                .andExpect(model().attributeExists("comments"));
    }

    @Test
    @DisplayName("корректно создать комментарий и перевести на страницу комментариев /comments/{bookId}")
    void addComment_doSubmitForm_pageWithNeedAttributes() throws Exception {
        var commentDto = CommentDto.builder()
                .id(FIRST_ID)
                .text(COMMENT_TEXT)
                .bookId(FIRST_ID)
                .build();

        mockMvc.perform(post("/comments/{bookId}/add", FIRST_ID)
                        .flashAttr("addComment", commentDto)
                        .header("Referer", REDIRECT_URL))
                .andExpect(model().attributeDoesNotExist("comments"))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/comments/" + FIRST_ID));
        verify(commentService, times(1)).add(commentDto);
    }

    @Test
    @DisplayName("вернуть на страницу comment_list при попытке создать комментарий с ошибочным полем")
    void addComment_doSubmitFormWithBindingResultError_pageWithNeedAttributes() throws Exception {
        var commentDto = CommentDto.builder()
                .id(SECOND_ID)
                .text("")
                .bookId(FIRST_ID)
                .build();

        when(commentService.getAllByBookId(FIRST_ID)).thenReturn(List.of(commentDto));

        mockMvc.perform(post("/comments/{bookId}/add", FIRST_ID)
                        .flashAttr("addComment", commentDto)
                        .header("Referer", REDIRECT_URL))
                .andExpect(status().isOk())
                .andExpect(view().name("comment_list"))
                .andExpect(model().attributeExists("bookId"))
                .andExpect(model().attribute("comments", hasSize(1)));
    }

    @Test
    @DisplayName("добавить на основную страницу данные для редактирования комментария")
    void editCommentPage_doGetRequest_pageWithNeedAttributes() throws Exception {
        var commentDto = CommentDto.builder()
                .id(SECOND_ID)
                .text(COMMENT_TEXT)
                .bookId(FIRST_ID)
                .build();

        when(commentService.getAllByBookId(FIRST_ID)).thenReturn(List.of(commentDto));

        mockMvc.perform(get("/comments/{bookId}/edit/{commentId}", FIRST_ID, SECOND_ID)
                        .param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("comment_list"))
                .andExpect(model().attributeExists("comments",
                        "editCommentId",
                        "editComment"
                ))
                .andExpect(model().attribute("bookId", equalTo(FIRST_ID)));
    }

    @Test
    @DisplayName("обновить комментарий и вернуться на общую страницу comment_list")
    void updateComment_doPostRequest_pageWithNeedAttributes() throws Exception {
        var commentDto = CommentDto.builder()
                .id(SECOND_ID)
                .text(COMMENT_TEXT)
                .bookId(FIRST_ID)
                .build();

        when(commentService.update(SECOND_ID, COMMENT_TEXT)).thenReturn(commentDto);

        mockMvc.perform(post("/comments/{bookId}/edit/{commentId}", FIRST_ID, SECOND_ID)
                        .flashAttr("editComment", commentDto))
                .andExpect(status().isOk())
                .andExpect(view().name("comment_list"))
                .andExpect(model().attributeExists("comments",
                        "editCommentId",
                        "editComment"
                ))
                .andExpect(model().attribute("bookId", equalTo(FIRST_ID)));
        verify(commentService, times(1)).update(SECOND_ID, COMMENT_TEXT);
    }

    @Test
    @DisplayName("корректно удалять жанр и переводить на нужную страницу /genre")
    void deleteComment_doGetRequest_redirectToCommentPage() throws Exception {
        mockMvc.perform(get("/comments/{bookId}/delete/{commentId}", FIRST_ID, SECOND_ID))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/comments/" + FIRST_ID));
        verify(commentService, times(1)).deleteById(SECOND_ID);
    }
}