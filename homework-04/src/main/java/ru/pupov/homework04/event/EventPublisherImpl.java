package ru.pupov.homework04.event;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventPublisherImpl implements EventPublisher {

    private final ApplicationEventPublisher publisher;

    @Override
    public void publish(String... payload) {
        publisher.publishEvent(new StartQuizEvent(this, payload));
    }
}
