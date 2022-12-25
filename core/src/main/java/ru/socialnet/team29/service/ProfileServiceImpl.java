package ru.socialnet.team29.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.socialnet.team29.model.Person;
import ru.socialnet.team29.model.ProfileResponse;
import ru.socialnet.team29.serviceInterface.ProfileService;

import java.time.OffsetDateTime;

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
        ProfileResponse profileResponse = ProfileResponse.builder()
                .error("string")
                .timestamp(System.currentTimeMillis())
                .person(Person.builder()
                        .id(1)
                        .firstName("Петр")
                        .lastName("Петрович")

                        .birthDate(OffsetDateTime.now())
                        .email("petr@mail.ru")
                        .phone("89100000000")
                        .photo("https://...../photos/image123.jpg")
                        .about("Родился в небольшой, но честной семье")
                        .city("Москва")
                        .country("Россия")
                        .build())
                .build();
        return profileResponse;
    }
}
