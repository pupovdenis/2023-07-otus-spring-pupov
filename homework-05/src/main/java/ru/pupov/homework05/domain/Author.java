package ru.pupov.homework05.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Author {

    private Long id;

    private String firstName;

    private String lastName;
}
