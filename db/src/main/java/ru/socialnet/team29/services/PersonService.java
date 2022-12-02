package ru.socialnet.team29.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jooq.exception.NoDataFoundException;
import org.springframework.stereotype.Service;
import ru.socialnet.team29.repository.PersonRepository;
import ru.socialnet.team29.domain.tables.records.PersonRecord;
import ru.socialnet.team29.mappers.PersonMapper;
import ru.socialnet.team29.model.Person;

@Service
@RequiredArgsConstructor
@Slf4j
public class PersonService {
    private final PersonRepository personRepository;
    private final PersonMapper personMapper;

    public Person getPersonByEmail(String email) {
        PersonRecord person = personRepository.findPersonByEmail(email);
        if (person != null) {
            return personMapper.PersonRecordToPerson(person);
        } else {
            throw new NoDataFoundException("No users found such email");
        }
    }

    public Person savePerson(Person person) {
        log.info("JOOQ добавил данные в БД {}", person);
        personRepository.insert(personMapper.PersonToPersonRecord(person));
        log.info("Отработал метод - personRepository.insert(personMapper.PersonToPersonRecord(person))! Данные попали в Базу Данных! ");
        return person;
    }
}
