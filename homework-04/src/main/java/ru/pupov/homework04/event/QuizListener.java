package ru.pupov.homework04.event;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import ru.pupov.homework04.service.QuizService;

@Component
@RequiredArgsConstructor
public class QuizListener {

    private final QuizService quizService;

    @EventListener
    public void onApplicationEvent(StartQuizEvent startQuizEvent) {
        quizService.run(startQuizEvent.getPayload());
    }
}
