package ru.pupov.domain;

public class Answer {

    private final String text;

    private final boolean isCorrectAnswer;

    public Answer(String text, boolean isCorrectAnswer) {
        this.text = text;
        this.isCorrectAnswer = isCorrectAnswer;
    }

    public String getText() {
        return text;
    }

    public boolean isCorrectAnswer() {
        return isCorrectAnswer;
    }
}
