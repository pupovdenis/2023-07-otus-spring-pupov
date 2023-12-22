package ru.pupov.homework08;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class Homework08Application {

    public static void main(String[] args) {
        SpringApplication.run(Homework08Application.class, args);
    }

}
