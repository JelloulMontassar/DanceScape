package com.dance.mo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mail.MailSenderValidatorAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableAsync

@SpringBootApplication(exclude = MailSenderValidatorAutoConfiguration.class)
public class MoApp {

    public static void main(String[] args) {
        SpringApplication.run(MoApp.class, args);
    }
}
