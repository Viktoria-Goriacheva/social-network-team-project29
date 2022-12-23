package ru.socialnet.team29.service;

import org.springframework.stereotype.Service;
import ru.socialnet.team29.answers.MessageAnswer;
import ru.socialnet.team29.dto.PersonLoginDTO;
import ru.socialnet.team29.model.Person;
import ru.socialnet.team29.model.ProfileResponse;
import ru.socialnet.team29.serviceInterface.LoginService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.time.OffsetDateTime;

@Service
public class LoginServiceImpl implements LoginService {
    @Override
    public ProfileResponse getProfile(PersonLoginDTO personLoginDTO) {
        return getPersonProfile();
    }


    public ProfileResponse getPersonProfile() {
        return ProfileResponse.builder()
                .error("string")
                .timestamp(System.currentTimeMillis())
                .person(Person.builder()
                        .id(1)
                        .firstName("Фёкла")
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
    }
    @Override
    public void setCookieToAnswer(HttpServletResponse response, MessageAnswer answer)
    {
        Cookie cookie = new Cookie("token", answer.getMessage());
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}
