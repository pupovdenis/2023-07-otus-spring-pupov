package ru.pupov.homework05.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Book {

    private Long id;

    private String name;

    private Author author;

    private Genre genre;
}
