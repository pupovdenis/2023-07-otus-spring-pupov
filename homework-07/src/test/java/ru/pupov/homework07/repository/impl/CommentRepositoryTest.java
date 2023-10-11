package ru.pupov.homework07.repository.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.pupov.homework07.entity.Comment;
import ru.pupov.homework07.repository.CommentRepository;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий Comment должен")
@DataJpaTest
class CommentRepositoryTest {

    public static final Long FIRST_COMMENT_ID = 1L;
    public static final String FIRST_COMMENT_TEXT = "Отличное чтиво. Дольше Санта-Барбары";
    public static final String NEW_COMMENT_TEXT = "Прочитал на одном дыхании";

    public static final Long FIRST_BOOK_ID = 1L;

    public static final int COMMENTS_COUNT = 1;

    @Autowired
    private CommentRepository commentRepository;

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
    void shouldFindAllCommentsByGenreId() {
        var books = commentRepository.findByBookId(FIRST_BOOK_ID);
        assertThat(books).hasSize(COMMENTS_COUNT);
    }
}