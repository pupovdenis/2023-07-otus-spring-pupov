package ru.pupov.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@Getter
public class Question {

    private final String question;

    private final List<Answer> answers;
}
