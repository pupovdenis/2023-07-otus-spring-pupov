package ru.pupov.homework08.repository.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import ru.pupov.homework08.model.Author;
import ru.pupov.homework08.model.Book;
import ru.pupov.homework08.model.Comment;
import ru.pupov.homework08.model.Genre;
import ru.pupov.homework08.repository.BookRepositoryCustom;

import java.util.List;

import static ru.pupov.homework08.constant.Constants.BOOK_COLLECTION_NAME;
import static ru.pupov.homework08.constant.Constants.UPDATE_ENTITY_MARKER;

@RequiredArgsConstructor
@Slf4j
public class BookRepositoryCustomImpl implements BookRepositoryCustom {

    private final MongoTemplate mongoTemplate;

    @Override
    public boolean updateCommentById(String bookId, String commentId, String text) {
        var query = Query.query(Criteria.where("_id").is(bookId));
        var update = new Update()
                .filterArray(new Criteria("comment._id").is(commentId))
                .set("comments.$[comment].text", text);
        var updateResult = mongoTemplate.updateFirst(query, update, BOOK_COLLECTION_NAME);
        return updateResult.getModifiedCount() == UPDATE_ENTITY_MARKER;
    }

    @Override
    public boolean deleteCommentByIds(String bookId, List<String> commentIds) {
        var query = Query.query(Criteria.where("_id").is(bookId));
        var update = new Update()
                .pull("comments", new Query(Criteria.where("_id").in(commentIds)));
        var updateResult = mongoTemplate.updateFirst(query, update, BOOK_COLLECTION_NAME);
        if (updateResult.getModifiedCount() != commentIds.size()) {
            log.warn("Have been deleted {} comments, but was needed to {}",
                    updateResult.getModifiedCount(), commentIds.size());
            return false;
        }
        return true;
    }

    @Override
    public boolean addComment(String bookId, String text) {
        var query = Query.query(Criteria.where("_id").is(bookId));
        var update = new Update()
                .push("comments", new Comment(text));
        var updateResult = mongoTemplate.updateFirst(query, update, BOOK_COLLECTION_NAME);
        return updateResult.getModifiedCount() == UPDATE_ENTITY_MARKER;
    }

    @Override
    public void deleteAuthor(String authorId) {
        Query query = new Query(Criteria.where("author._id").is(authorId));
        Update update = new Update().set("author", new Author());
        mongoTemplate.updateMulti(query, update, Book.class);
    }

    @Override
    public void deleteGenre(String genreId) {
        Query query = new Query(Criteria.where("genre._id").is(genreId));
        Update update = new Update().set("genre", new Genre());
        mongoTemplate.updateMulti(query, update, Book.class);
    }
}
