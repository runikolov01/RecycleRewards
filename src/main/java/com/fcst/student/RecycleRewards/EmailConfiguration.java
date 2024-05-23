package com.fcst.student.RecycleRewards;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class EmailConfiguration {

    private final Environment environment;

    public EmailConfiguration(Environment environment) {
        this.environment = environment;
    }

    public void printEmailSettings() {
        String username = environment.getProperty("spring.mail.username");
        String password = environment.getProperty("spring.mail.password");

        System.out.println("Email username: " + username);
        System.out.println("Email password: " + password);
    }
}