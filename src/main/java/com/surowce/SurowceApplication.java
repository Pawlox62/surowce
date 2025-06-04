package com.surowce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SurowceApplication {
    public static void main(String[] args) {
        SpringApplication.run(SurowceApplication.class, args);
    }
}
