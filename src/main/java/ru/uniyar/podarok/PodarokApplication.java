package ru.uniyar.podarok;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Главный класс Spring Boot приложения.
 * Это основной класс, запускающий приложение и инициирующий его конфигурацию.
 */
@SpringBootApplication
public class PodarokApplication {
    /**
     * Главный метод для запуска Spring Boot приложения.
     * Этот метод является точкой входа в приложение.
     *
     * @param args аргументы командной строки, передаваемые при запуске приложения.
     */
    public static void main(String[] args) {
        SpringApplication.run(PodarokApplication.class, args);
    }
}
