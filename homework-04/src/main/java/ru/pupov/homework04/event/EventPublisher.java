package ru.pupov.homework04.event;

public interface EventPublisher {

    void publish(String... payload);
}
