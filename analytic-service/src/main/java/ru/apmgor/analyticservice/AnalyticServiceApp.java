package ru.apmgor.analyticservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class AnalyticServiceApp {

    public static void main(String[] args) {
        SpringApplication.run(AnalyticServiceApp.class, args);
    }

}
