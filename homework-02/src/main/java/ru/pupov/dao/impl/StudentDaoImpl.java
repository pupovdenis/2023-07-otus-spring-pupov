package ru.pupov.dao.impl;

import org.springframework.stereotype.Component;
import ru.pupov.dao.StudentDao;
import ru.pupov.domain.Student;
import ru.pupov.service.IOService;

@Component
public class StudentDaoImpl implements StudentDao {

    private static final String ASK_FIRST_NAME_MESSAGE = "\nEnter your first name: ";
    private static final String ASK_LAST_NAME_MESSAGE = "Enter last name: ";

    private final IOService ioService;

    public StudentDaoImpl(IOService ioService) {
        this.ioService = ioService;
    }

    @Override
    public Student getStudent() {
        var firstName = ioService.readStringWithPrompt(ASK_FIRST_NAME_MESSAGE, true);
        var lastName = ioService.readStringWithPrompt(ASK_LAST_NAME_MESSAGE, true);
        return new Student(firstName, lastName);
    }
}
