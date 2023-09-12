package ru.pupov.homework05.domain;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Genre {

    private Long id;

    private String name;

    private List<Book> books;
}
