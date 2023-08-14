package ru.pupov.converter.impl;

import org.springframework.stereotype.Component;
import ru.pupov.converter.QuestionConverter;
import ru.pupov.domain.Question;

@Component
public class QuestionConverterImpl implements QuestionConverter {

    @Override
    public String toQuizString(Question question) {
        var answers = question.getAnswers();
        var stringBuilder = new StringBuilder();
        stringBuilder
                .append("\n")
                .append(question.getQuestion())
                .append("\n");
        for (int i = 0; i < answers.size(); i++) {
            stringBuilder
                    .append(i + 1)
                    .append(") ")
                    .append(answers.get(i).getText())
                    .append("\n");
        }
        return stringBuilder.toString();
    }
}
