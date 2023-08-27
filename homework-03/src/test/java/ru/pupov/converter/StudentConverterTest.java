package ru.pupov.converter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.pupov.domain.Student;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("класс StudentConverterTest")
class StudentConverterTest {

    public static final String TEST_FIRST_NAME = "TestFirstName";

    public static final String TEST_LAST_NAME = "TestLastName";

    private Student student;

    private final StudentConverter studentConverter = new StudentConverter();

    @BeforeEach
    void SetUp() {
        student = new Student(TEST_FIRST_NAME, TEST_LAST_NAME);
    }

    @DisplayName("Корректно конвертирует сущность студента в строку для вывода")
    @Test
    void shouldConvertQuestionToQuizString() {
        var result = studentConverter.convert(student);
        assertThat(result).isEqualTo(getCorrectResult());
    }

    private String getCorrectResult() {
        return TEST_FIRST_NAME + " " + TEST_LAST_NAME;
    }

}