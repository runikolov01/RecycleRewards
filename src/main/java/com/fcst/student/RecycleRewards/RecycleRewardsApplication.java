package com.fcst.student.RecycleRewards;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

@SpringBootApplication
public class RecycleRewardsApplication {

    public static void main(String[] args) {
        SpringApplication.run(RecycleRewardsApplication.class, args);
    }

    @Bean
    public Dotenv dotenv() {
        return Dotenv.configure().ignoreIfMalformed().ignoreIfMissing().load();
    }

    @Bean
    public EmailConfiguration emailConfiguration(Environment environment) {
        return new EmailConfiguration(environment);
    }

}
