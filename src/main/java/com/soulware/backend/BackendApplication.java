package com.soulware.backend;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Slf4j(topic = "BackendApplication")
@EnableJpaAuditing
@SpringBootApplication
public class BackendApplication {

    private static Environment environment;

    @Autowired
    public BackendApplication(Environment environment) {
        BackendApplication.environment = environment;
    }

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
        BackendApplication app = new BackendApplication(environment);
        app.contextLoads();
    }

    public void contextLoads() {
        log.info("BackendApplication 실행");
        log.info("profile 값 :: {}", environment.getProperty("spring.profiles.active"));
    }

}
