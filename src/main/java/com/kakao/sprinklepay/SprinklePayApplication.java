package com.kakao.sprinklepay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SprinklePayApplication {

    public static void main(String[] args) {
        SpringApplication.run(SprinklePayApplication.class, args);
    }

}
