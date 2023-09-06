package ru.pupov.homework04.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.pupov.homework04.domain.Student;
import ru.pupov.homework04.service.LocalizationIOService;
import ru.pupov.homework04.service.StudentService;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final LocalizationIOService localizationIOService;

    @Override
    public Student getStudent() {
        var firstName = localizationIOService.readStringWithPrompt("ask.first.name.message", true, true);
        var lastName = localizationIOService.readStringWithPrompt("ask.last.name.message", true, true);
        return new Student(firstName, lastName);
    }

    @Override
    public Student getStudent(String firstName, String lastName) {
        if (!firstName.isBlank() && !lastName.isBlank()) {
            return new Student(firstName, lastName);
        }
        throw new RuntimeException("payload not correct");
    }
}
