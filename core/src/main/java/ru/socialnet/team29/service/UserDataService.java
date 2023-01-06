package ru.socialnet.team29.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.socialnet.team29.answers.MessageAnswer;
import ru.socialnet.team29.answers.NegativeResponseUserRegister;
import ru.socialnet.team29.answers.ResponseUserRegister;
import ru.socialnet.team29.answers_interface.CommonAnswer;
import ru.socialnet.team29.model.Person;
import ru.socialnet.team29.payloads.ContactConfirmationPayload;
import ru.socialnet.team29.serviceInterface.feign.DBConnectionFeignInterfacePerson;

import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserDataService {

    private final DBConnectionFeignInterfacePerson feignInterface;
    private final CaptchaService captchaService;
    private final EmailService emailService;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public void setPasswordEncoder(@Lazy PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public CommonAnswer saveNewUserInDb(ContactConfirmationPayload payload) {
        if (!payload.getPassword1().equals(payload.getPassword2()) | payload.getCode().isBlank()) {
            return getAnswer("invalid_request", "Поля пароль и подтверждение пароля не совпадают, или не заполнено поле code.");
        }
        else if (isRegisteredMail(payload.getEmail())){
            return getAnswer("invalid_request", "Такой адрес почты уже зарегистрирован!");
        } else {
            Person person = Person.builder()
                    .firstName(payload.getFirstName())
                    .lastName(payload.getLastName())
                    .password(passwordEncoder.encode(payload.getPassword1()))
                    .email(payload.getEmail())
                    .regDate(OffsetDateTime.now())
                    .createdOn(OffsetDateTime.now())
                    .isDeleted(false)
                    .isOnline(false)
                    .isBlocked(false)
                    .build();
            try {
                log.info("Заполняем данные пользователя при регистрации {}", person);
                feignInterface.savePerson(person);
                emailService.sendEmail("Регистрация нового пользователя", "Регистрация прошла успешно", person.getEmail());
                log.info("Письмо было отправлено.");
            } catch (Exception e) {
                return getAnswer("invalid_request", "Во время сохранения произошла ошибка.");
            }
            if (captchaService.checkCaptcha(payload.getToken())) {
                return getAnswer("", "");
            }
        }
        return getAnswer("invalid_request", "captcha введена неверно");
    }

    private CommonAnswer getAnswer(String error, String description) {
        if (!error.isBlank()) {
            NegativeResponseUserRegister negativeResponseUserRegister = new NegativeResponseUserRegister();
            negativeResponseUserRegister.setErrorDescription(description);
            negativeResponseUserRegister.setError("invalid_request");
            return negativeResponseUserRegister;
        }
        ResponseUserRegister responseUserRegister = new ResponseUserRegister();
        responseUserRegister.setTimestamp(System.currentTimeMillis());
        responseUserRegister.setMessageAnswer(new MessageAnswer("ok"));
        return responseUserRegister;
    }

    public boolean isRegisteredMail(String email) {
        return feignInterface.isRegisteredMail(email);
    }

    public Person getCurrentAccount() {
        return feignInterface.getPersonByEmail(
                SecurityContextHolder.getContext().getAuthentication().getName());
    }
}
