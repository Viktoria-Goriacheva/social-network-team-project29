package ru.socialnet.team29.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.socialnet.team29.model.City;
import ru.socialnet.team29.model.Country;
import ru.socialnet.team29.model.Person;
import ru.socialnet.team29.model.ProfileResponse;
import ru.socialnet.team29.model.enums.BlockStatus;
import ru.socialnet.team29.model.enums.MessagePermission;
import ru.socialnet.team29.serviceInterface.ProfileService;

import java.time.LocalDateTime;

@Slf4j
@Service
public class ProfileServiceImpl implements ProfileService {

    @Override
    public ProfileResponse getProfile(ProfileResponse profileResponse) {
        var responseProfile = createDefaultProfile();
        log.info("Create new profileResponse {}", responseProfile);
        return responseProfile;
    }

    public ProfileResponse createDefaultProfile() {
        return ProfileResponse.builder()
                .error("string")
                .timestamp(LocalDateTime.now())
                .person(Person.builder()
                        .id(1L)
                        .firstName("Петр")
                        .lastName("Петрович")
                        .registrationDate(LocalDateTime.now())
                        .birthDate(LocalDateTime.now())
                        .email("petr@mail.ru")
                        .phone("89100000000")
                        .photo("https://...../photos/image123.jpg")
                        .about("Родился в небольшой, но честной семье")
                        .city(new City(1L, "Москва"))
                        .country(new Country(1L, "Россия"))
                        .messagesPermission(MessagePermission.ALL)
                        .lastOnlineTime(LocalDateTime.now())
                        .isBlocked(BlockStatus.UNBLOCKED)
                        .build())
                .build();

    }

}
