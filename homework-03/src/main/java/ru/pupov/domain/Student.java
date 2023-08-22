package ru.pupov.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class Student {

    private String firstName;

    private String lastName;

    @Override
    public String toString() {
        return String.format("%s %s", firstName, lastName);
    }
}
