package ru.pupov;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.pupov.service.QuizService;

public class Main {

    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("/spring-context.xml");
        var quizService = context.getBean(QuizService.class);
        quizService.run();
    }
}
