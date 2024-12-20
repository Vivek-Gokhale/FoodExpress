package com.foodexpress;

import org.springframework.context.ApplicationContext;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class FoodExpressApplication {

    public static void main(String[] args) {
        // Use Spring's ApplicationContext
        ApplicationContext context = SpringApplication.run(FoodExpressApplication.class, args);
           
    }
}
