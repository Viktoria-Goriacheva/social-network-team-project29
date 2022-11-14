package ru.socialnet.team29.service;

import org.springframework.stereotype.Service;
import ru.socialnet.team29.dto.PersonLoginDTO;
import ru.socialnet.team29.model.*;
import ru.socialnet.team29.model.enums.BlockStatus;
import ru.socialnet.team29.model.enums.MessagePermission;
import ru.socialnet.team29.serviceInterface.LoginService;

import java.time.LocalDateTime;

@Service
public class LoginServiceImpl implements LoginService {
    @Override
    public ProfileResponse getProfile(PersonLoginDTO personLoginDTO) {
        var loginProfile = getPersonProfile();
        return loginProfile;
    }

    public ProfileResponse getPersonProfile() {
        return ProfileResponse.builder()
                .error("string")
                .timestamp(System.currentTimeMillis())
                .person(Person.builder()
                        .id(1)
                        .firstName("Фёкла")
                        .lastName("Петрович")
                        .registrationDate(System.currentTimeMillis())
                        .birthDate(System.currentTimeMillis())
                        .email("petr@mail.ru")
                        .phone("89100000000")
                        .photo("https://...../photos/image123.jpg")
                        .about("Родился в небольшой, но честной семье")
                        .city(new City(1L, "Москва"))
                        .country(new Country(1L, "Россия"))
                        .build())
                .build();
    }
}
