package com.onlinemarket.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class OnlinemarketApplication {
    public static void main(String[] args) {
        SpringApplication.run(OnlinemarketApplication.class, args);
        System.out.println("Server running");
    }

}
