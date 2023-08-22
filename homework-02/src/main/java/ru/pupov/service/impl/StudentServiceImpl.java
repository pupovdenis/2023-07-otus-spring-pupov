package ru.pupov.service.impl;

import org.springframework.stereotype.Service;
import ru.pupov.domain.Student;
import ru.pupov.service.IOService;
import ru.pupov.service.StudentService;

@Service
public class StudentServiceImpl implements StudentService {

    private static final String ASK_FIRST_NAME_MESSAGE = "\nEnter your first name: ";

    private static final String ASK_LAST_NAME_MESSAGE = "Enter last name: ";

    private final IOService ioService;

    public StudentServiceImpl(IOService ioService) {
        this.ioService = ioService;
    }

    @Override
    public Student getStudent() {
        var firstName = ioService.readStringWithPrompt(ASK_FIRST_NAME_MESSAGE, true);
        var lastName = ioService.readStringWithPrompt(ASK_LAST_NAME_MESSAGE, true);
        return new Student(firstName, lastName);
    }
}
