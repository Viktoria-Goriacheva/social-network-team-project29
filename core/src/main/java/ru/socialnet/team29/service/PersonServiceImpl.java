package ru.socialnet.team29.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.socialnet.team29.model.Person;
import ru.socialnet.team29.serviceInterface.PersonService;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService {
    private final Person person;


    @Override
    public Person createNewTestPerson() {
        person.setId(5);
        person.setFirstName("Ivan");
        person.setLastName("Ivanov");
        person.setEmail("ivan@mail.ru");
        person.setPhone("8-917-338-20-20");
        person.setPhoto("link on url photo");
        person.setAbout("Was born in Russia");
        person.setCity("Moscow");
        person.setCountry("Russia");
        person.setToken("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJwb0Bwby5jbyIsImV4cCI6MTY2OTQwNTUxNywiaW" +
                "F0IjoxNjY5MzY5NTE3fQ.a2JkHCAvfpM0776XVMkUgtGeiBcCTptEtQEj_U8qqso");
        person.setStatusCode("FRIEND");
        person.setRegDate(LocalDateTime.now());
        person.setBirthDate(LocalDateTime.of(1979,9,9,0,0));
        person.setMessagesPermission("string");
        person.setLastOnlineTime(LocalDateTime.now());
        person.setIsDeleted(false);
        person.setIsBlocked(false);
        person.setIsOnline(true);
        person.setPhotoId("3");
        person.setPhotoName("my photo");
        person.setRole("USER");
        person.setCreatedOn(LocalDateTime.now());
        person.setUpdatedOn(LocalDateTime.now());
        person.setPassword("Oleg1256$");

        return person;
    }

    public Person getPersonByEmail(String email){
        return person;
    }
}
