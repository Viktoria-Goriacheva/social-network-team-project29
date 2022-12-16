package ru.socialnet.team29;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import ru.socialnet.team29.config.EmailSenderConfig;

@SpringBootApplication
@EnableFeignClients(basePackages = {"ru.socialnet.team29.serviceInterface"})
@ConfigurationPropertiesScan("ru.socialnet.team29")
public class SocialNetCoreApplication {
    public static void main(String[] args) {
        SpringApplication.run(SocialNetCoreApplication.class, args);
    }
}
