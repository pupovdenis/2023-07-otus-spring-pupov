package ru.pupov.dao.impl;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import ru.pupov.config.FileNameProvider;
import ru.pupov.dao.QuestionDao;
import ru.pupov.domain.Answer;
import ru.pupov.domain.Question;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

@Component
public class QuestionDaoImpl implements QuestionDao {

    private static final String CSV_DELIMITER = ";";

    private static final int MIN_CSV_STRINGS = 2;

    private static final int QUESTION_INDEX = 0;

    private static final int CORRECT_ANSWER_INDEX = 1;

    private final FileNameProvider fileNameProvider;

    public QuestionDaoImpl(FileNameProvider fileNameProvider) {
        this.fileNameProvider = fileNameProvider;
    }

    @Override
    public List<Question> getAll() {
        var classloader = Thread.currentThread().getContextClassLoader();
        var classPathResource = new ClassPathResource(fileNameProvider.getDataResourcePath(), classloader);
        List<Question> questions;
        try (var reader = new BufferedReader(new InputStreamReader(classPathResource.getInputStream()))) {
            questions = getQuestions(reader.lines());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (questions == null) {
            throw new RuntimeException("failed to get any question");
        }
        return questions;
    }

    private List<Question> getQuestions(Stream<String> lines) throws IOException {
        return lines
                .map(str -> str.split(CSV_DELIMITER))
                .filter(strings -> strings.length >= MIN_CSV_STRINGS)
                .map(this::getQuestion)
                .collect(toList());
    }

    private Question getQuestion(String[] strings) {
        try {
            var question = strings[QUESTION_INDEX];
            var answers = new ArrayList<Answer>();
            for (int i = 0; i < strings.length; i++) {
                if (i == QUESTION_INDEX) {
                    continue;
                }
                var answer = new Answer(strings[i], i == CORRECT_ANSWER_INDEX);
                answers.add(answer);
            }
            return new Question(question, answers);
        } catch (Exception e) {
            throw new RuntimeException("failed to get question", e);
        }
    }
}
