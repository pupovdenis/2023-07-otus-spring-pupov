package ru.pupov.homework08.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.pupov.homework08.model.Author;

public interface AuthorRepository extends MongoRepository<Author, String> {
}
