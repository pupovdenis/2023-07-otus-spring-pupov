package ru.pupov.homework08.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.pupov.homework08.model.Book;

import java.util.List;

public interface BookRepository extends MongoRepository<Book, String>, BookRepositoryCustom {

    List<Book> findByAuthorId(String authorId);

    List<Book> findByGenreId(String genreId);
}
