package ru.pupov.homework06.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.pupov.homework06.entity.Comment;
import ru.pupov.homework06.repository.CommentRepository;

import java.util.List;
import java.util.Optional;

import static org.springframework.data.jpa.repository.EntityGraph.EntityGraphType.FETCH;

@Repository
@RequiredArgsConstructor
public class CommentRepositoryJpa implements CommentRepository {

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public Comment save(Comment comment) {
        if (comment.getId() != null) {
            return entityManager.merge(comment);
        }
        entityManager.persist(comment);
        return comment;
    }

    @Override
    public Optional<Comment> findById(Long id) {
        return Optional.ofNullable(entityManager.find(Comment.class, id));
    }

    @Override
    public boolean deleteById(Long id) {
        var comment = entityManager.find(Comment.class, id);
        entityManager.remove(comment);
        return true;
    }

    @Override
    public List<Comment> findByBookId(Long bookId) {
        var entityGraph = entityManager.getEntityGraph("comments-by-book-entity-graph");
        var query = entityManager.createQuery("""
                select c
                from Comment c
                where c.book.id = :book_id
                """, Comment.class);
        query.setParameter("book_id", bookId);
        query.setHint(FETCH.getKey(), entityGraph);
        return query.getResultList();
    }
}
