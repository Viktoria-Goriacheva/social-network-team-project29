package ru.socialnet.team29.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import ru.socialnet.team29.config.EmailSenderConfig;
import ru.socialnet.team29.model.Person;
import ru.socialnet.team29.responses.PasswordResponse;
import ru.socialnet.team29.serviceInterface.feign.DBConnectionFeignInterface;
import ru.socialnet.team29.serviceInterface.feign.DBConnectionFeignInterfacePerson;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {
    private final EmailSenderConfig emailSenderConfig;
    private final DBConnectionFeignInterfacePerson feignInterface;

    public String getPersonByEmailForGetToken(String email) {
        log.info(email + "  email from emailService");
        Person personByEmail = feignInterface.getPersonByEmail(email);
        return personByEmail.getToken();
    }

    public MimeMessage generateResetTokenEmail(String email) throws MessagingException {
        String token = getPersonByEmailForGetToken(email);
        String url = emailSenderConfig.getUrl() + token;
        log.info(url + " url from email service");
        return sendEmail("Reset Password", "ссылка для восстановления пароля" + " \r\n" + url, email);
    }

    @Async
    public MimeMessage sendEmail(String subject, String body, String email) throws MessagingException {
        MimeMessage message = emailSenderConfig.javaMailSenderBean().createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setTo(String.valueOf(email));
        helper.setSubject(subject);
        helper.setText(body);
        emailSenderConfig.javaMailSenderBean().send(message);
        return message;
    }

    public Boolean isTokenExist(String token, PasswordResponse password) {
        Person personByToken = feignInterface.getPersonByToken(token);
        if (personByToken.getToken() != null) {
            personByToken.setPassword(password.toString());
            personByToken.setToken(UUID.randomUUID().toString());
            feignInterface.updatePerson(personByToken);
            return true;
        }
        return false;
    }
}
