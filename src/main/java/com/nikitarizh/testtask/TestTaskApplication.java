package com.nikitarizh.testtask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TestTaskApplication {

    public static final Logger logger = LoggerFactory.getLogger(TestTaskApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(TestTaskApplication.class, args);
    }
}
