package ru.pupov.converter;

import org.springframework.core.convert.converter.Converter;
import ru.pupov.domain.Student;

public class StudentConverter implements Converter<Student, String> {
    @Override
    public String convert(Student sourceStudent) {
        return String.format("%s %s", sourceStudent.getFirstName(), sourceStudent.getLastName());
    }
}
