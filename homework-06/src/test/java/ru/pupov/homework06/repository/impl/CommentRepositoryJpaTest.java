package ru.pupov.homework06.repository.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.pupov.homework06.entity.Comment;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий Comment должен")
@DataJpaTest
@Import(CommentRepositoryJpa.class)
class CommentRepositoryJpaTest {

    public static final Long FIRST_COMMENT_ID = 1L;
    public static final String FIRST_COMMENT_TEXT = "Отличное чтиво. Дольше Санта-Барбары";
    public static final Long THIRD_COMMENT_ID = 3L;
    public static final String NEW_COMMENT_TEXT = "Прочитал на одном дыхании";

    public static final Long FIRST_BOOK_ID = 1L;

    public static final int COMMENTS_COUNT = 1;

    @Autowired
    private CommentRepositoryJpa commentRepository;

    @Autowired
    private TestEntityManager em;

    @DisplayName("сохранять новый комментарий")
    @Test
    void shouldSaveNewComment() {
        var actualComment = Comment.builder()
                .id(null)
                .text(NEW_COMMENT_TEXT)
                .build();
        var savedComment = commentRepository.save(actualComment);

        em.flush();
        em.clear();

        var expectedComment = commentRepository.findById(savedComment.getId());
        assertThat(expectedComment).isNotNull().get()
                .hasFieldOrPropertyWithValue("text", NEW_COMMENT_TEXT);
    }

    @DisplayName("возвращать комментарий по его id")
    @Test
    void shouldFindCommentById() {
        var comment = commentRepository.findById(FIRST_COMMENT_ID)
                .orElse(null);
        assertThat(comment).isNotNull()
                .hasFieldOrPropertyWithValue("text", FIRST_COMMENT_TEXT);
        assertThat(comment.getBook()).isNotNull();
    }

    @DisplayName("возвращать список всех комментариев по id книги")
    @Test
    void shouldFindAllBooksByGenreId() {
        var books = commentRepository.findByBookId(FIRST_BOOK_ID);
        assertThat(books).hasSize(COMMENTS_COUNT);
    }

    @DisplayName("обновлять существующий комментарий")
    @Test
    void shouldUpdateExistingComment() {
        var actualComment = commentRepository.findById(THIRD_COMMENT_ID).orElse(null);
        assertThat(actualComment).isNotNull();
        assertThat(actualComment.getText()).isNotEqualTo(NEW_COMMENT_TEXT);

        actualComment.setText(NEW_COMMENT_TEXT);
        commentRepository.save(actualComment);

        em.flush();
        em.clear();

        var expectedComment = commentRepository.findById(THIRD_COMMENT_ID).orElse(null);
        assertThat(expectedComment).isNotNull()
                .hasFieldOrPropertyWithValue("text", NEW_COMMENT_TEXT);
    }

    @DisplayName("удалять комментарий по его id")
    @Test
    void shouldDeleteCommentById() {
        var comment = em.find(Comment.class, FIRST_COMMENT_ID);
        assertThat(comment).isNotNull();

        commentRepository.deleteById(FIRST_COMMENT_ID);

        em.flush();
        em.clear();

        comment = em.find(Comment.class, FIRST_COMMENT_ID);
        assertThat(comment).isNull();
    }
}