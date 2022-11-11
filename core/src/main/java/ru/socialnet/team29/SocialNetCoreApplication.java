package ru.socialnet.team29;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = {"ru.socialnet.team29.serviceInterface"})
public class SocialNetCoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(SocialNetCoreApplication.class, args);
	}
}
