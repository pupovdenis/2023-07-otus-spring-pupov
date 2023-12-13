package ru.pupov.homework08.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.pupov.homework08.model.Genre;

public interface GenreRepository extends MongoRepository<Genre, String> {
}
