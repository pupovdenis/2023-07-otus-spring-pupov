package ru.pupov.homework04.domain;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Answer {

    private final String text;

    private final boolean isCorrectAnswer;

    public String getText() {
        return text;
    }

    public boolean isCorrectAnswer() {
        return isCorrectAnswer;
    }
}
