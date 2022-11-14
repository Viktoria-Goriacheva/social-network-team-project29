package ru.socialnet.team29;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class SocialNetDatabaseApp {
    public static void main(String[] args) {
        SpringApplication.run(SocialNetDatabaseApp.class,args);
    }
}
