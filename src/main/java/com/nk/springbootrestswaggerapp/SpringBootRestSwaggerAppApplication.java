package com.nk.springbootrestswaggerapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {"com.nk"})
@EntityScan(basePackages = {"com.nk.model"})
@EnableJpaRepositories(basePackages = {"com.nk.repository"})
public class SpringBootRestSwaggerAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootRestSwaggerAppApplication.class, args);
    }

}
