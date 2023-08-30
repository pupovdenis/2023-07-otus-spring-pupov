package ru.pupov.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.pupov.domain.Student;
import ru.pupov.service.LocalizationIOService;
import ru.pupov.service.StudentService;

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
}
