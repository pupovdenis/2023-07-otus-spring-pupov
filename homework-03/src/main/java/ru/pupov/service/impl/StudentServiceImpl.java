package ru.pupov.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.pupov.domain.Student;
import ru.pupov.service.IOService;
import ru.pupov.service.LocalizationService;
import ru.pupov.service.StudentService;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final IOService ioService;

    private final LocalizationService localizationService;

    @Override
    public Student getStudent() {
        var askFirstNameMessage = "\n" + localizationService.getMessage("ask.first.name.message") + " ";
        var firstName = ioService.readStringWithPrompt(askFirstNameMessage, true);
        var askLastNameMessage = localizationService.getMessage("ask.last.name.message") + " ";
        var lastName = ioService.readStringWithPrompt(askLastNameMessage, true) + " ";
        return new Student(firstName, lastName);
    }
}
