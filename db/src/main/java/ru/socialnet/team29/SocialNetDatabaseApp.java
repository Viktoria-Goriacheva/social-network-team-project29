package ru.socialnet.team29;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Import;
import ru.socialnet.team29.services.LaunchCheckBirthdate;


@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class SocialNetDatabaseApp {
    public static void main(String[] args) {
        SpringApplication.run(SocialNetDatabaseApp.class,args);
    }
}
