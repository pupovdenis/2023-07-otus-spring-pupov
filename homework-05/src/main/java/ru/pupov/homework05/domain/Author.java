package ru.pupov.homework05.domain;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Author {

    private Long id;

    private String firstName;

    private String lastName;

    private List<Book> books;
}
