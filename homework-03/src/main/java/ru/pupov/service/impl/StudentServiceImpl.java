package ru.pupov.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.pupov.domain.Student;
import ru.pupov.service.IOLFacade;
import ru.pupov.service.StudentService;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final IOLFacade iolFacade;

    @Override
    public Student getStudent() {
        var firstName = iolFacade.readStringWithPrompt("ask.first.name.message", true, true);
        var lastName = iolFacade.readStringWithPrompt("ask.last.name.message", true, true);
        return new Student(firstName, lastName);
    }
}
