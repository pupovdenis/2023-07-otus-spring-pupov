package ru.pupov.homework07.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.pupov.homework07.entity.Genre;

public interface GenreRepository extends JpaRepository<Genre, Long> {
}
