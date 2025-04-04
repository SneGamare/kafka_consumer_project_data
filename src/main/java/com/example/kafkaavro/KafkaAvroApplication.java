package com.example.kafkaavro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableWebMvc
@ComponentScan(basePackages = {"com.example.kafkaavro", "com.example.kafkaavro.controller", "com.example.kafkaavro.service"})
public class KafkaAvroApplication {
    public static void main(String[] args) {
        SpringApplication.run(KafkaAvroApplication.class, args);
    }
} 