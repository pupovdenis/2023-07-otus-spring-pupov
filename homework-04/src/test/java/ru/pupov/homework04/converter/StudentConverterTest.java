package ru.pupov.homework04.converter;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.pupov.homework04.domain.Student;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("класс StudentConverterTest")
@SpringBootTest(classes = StudentConverter.class)
class StudentConverterTest {

    public static final String TEST_FIRST_NAME = "TestFirstName";

    public static final String TEST_LAST_NAME = "TestLastName";

    @Autowired
    private StudentConverter studentConverter;

    @DisplayName("Корректно конвертирует сущность студента в строку для вывода")
    @Test
    void shouldConvertQuestionToQuizString() {
        var student = new Student(TEST_FIRST_NAME, TEST_LAST_NAME);
        var result = studentConverter.convert(student);
        assertThat(result).isEqualTo(getCorrectResult());
    }

    private String getCorrectResult() {
        return TEST_FIRST_NAME + " " + TEST_LAST_NAME;
    }
}