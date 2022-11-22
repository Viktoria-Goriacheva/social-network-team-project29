package ru.socialnet.team29.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jooq.exception.NoDataFoundException;
import org.springframework.stereotype.Service;
import ru.socialnet.team29.PersonRepository;
import ru.socialnet.team29.domain.tables.records.PersonRecord;
import ru.socialnet.team29.mappers.PersonMapperImpl;
import ru.socialnet.team29.model.Person;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PersonService {
    private final PersonRepository personRepository;
    private final PersonMapperImpl personMapper;

    public Person getPersonByEmail(String email) {
        List<PersonRecord> persons = personRepository.findPersonByEmail(email);

        if (persons.size() != 0) {
            return personMapper.PersonRecordToPerson(persons.get(0));
        } else {
            throw new NoDataFoundException("No users found such email");
        }
    }

}
