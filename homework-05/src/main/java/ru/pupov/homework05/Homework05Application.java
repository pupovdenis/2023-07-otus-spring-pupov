package ru.pupov.homework05;

import org.h2.tools.Console;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.SQLException;

@SpringBootApplication
public class Homework05Application {

    public static void main(String[] args) throws SQLException {
        SpringApplication.run(Homework05Application.class, args);
        Console.main(args);
    }

}
