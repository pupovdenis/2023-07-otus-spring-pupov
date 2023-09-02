package ru.pupov.homework04.converter;

import org.springframework.core.convert.converter.Converter;
import ru.pupov.homework04.domain.Question;

public class QuestionConverter implements Converter<Question, String> {
    @Override
    public String convert(Question sourceQuestion) {
        var answers = sourceQuestion.getAnswers();
        var stringBuilder = new StringBuilder();
        stringBuilder
                .append("\n")
                .append(sourceQuestion.getQuestion())
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
