package ru.socialnet.team29.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.socialnet.team29.answers.MessageAnswer;
import ru.socialnet.team29.answers.NegativeResponseUserRegister;
import ru.socialnet.team29.answers.ResponseUserRegister;
import ru.socialnet.team29.answers_interface.CommonAnswer;
import ru.socialnet.team29.model.Person;
import ru.socialnet.team29.payloads.ContactConfirmationPayload;
import ru.socialnet.team29.serviceInterface.DBConnectionFeignInterface;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserDataService {

    private final DBConnectionFeignInterface feignInterface;

    public CommonAnswer saveNewUserInDb(ContactConfirmationPayload payload) {
        if (!payload.getPasswd1().equals(payload.getPasswd2()) | payload.getCode().isBlank()) {
            return getAnswer("invalid_request", "Поля пароль и подтверждение пароля не совпадают, или не заполнено поле code.");
        } else {
            Person person = Person.builder()
                    .firstName(payload.getFirstName())
                    .lastName(payload.getLastName())
                    .password(payload.getPasswd1())
                    .confirmationCode(payload.getCode())
                    .build();
            try {
                feignInterface.savePerson(person);
            } catch (Exception e) {
                return getAnswer("invalid_request", "Во время сохранения произошла ошибка.");
            }
            return getAnswer("", "");
        }
    }

    private CommonAnswer getAnswer(String error, String description) {
        if (!error.isBlank()) {
            NegativeResponseUserRegister negativeResponseUserRegister = new NegativeResponseUserRegister();
            negativeResponseUserRegister.setErrorDescription(description);
            negativeResponseUserRegister.setError("invalid_request");
            return negativeResponseUserRegister;
        }
        ResponseUserRegister responseUserRegister = new ResponseUserRegister();
        responseUserRegister.setLocalDateTime(LocalDateTime.now());
        responseUserRegister.setMessageAnswer(new MessageAnswer("ok"));
        return responseUserRegister;
    }
}
