package ru.pupov.homework04.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

public class StartQuizEvent extends ApplicationEvent {

    @Getter
    private final String[] payload;

    public StartQuizEvent(Object source, String... payload) {
        super(source);
        this.payload = payload;
    }
}
