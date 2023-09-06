package ru.pupov.homework04.service;

import ru.pupov.homework04.domain.Student;

public interface StudentService {
    Student getStudent();

    Student getStudent(String firstName, String lastName);
}
