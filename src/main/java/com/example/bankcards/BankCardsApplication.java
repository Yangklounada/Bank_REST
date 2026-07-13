package com.example.bankcards;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Точка входа в систему управления банковскими картами.
 * Запускает Spring Boot приложение.
 */
@SpringBootApplication
public class BankCardsApplication {
    public static void main(String[] args) {
        SpringApplication.run(BankCardsApplication.class, args);
    }
}
