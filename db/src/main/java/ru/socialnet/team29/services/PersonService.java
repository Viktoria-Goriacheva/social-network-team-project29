package ru.socialnet.team29.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jooq.exception.NoDataFoundException;
import org.springframework.stereotype.Service;
import ru.socialnet.team29.PersonRepository;
import ru.socialnet.team29.domain.tables.records.PersonRecord;
import ru.socialnet.team29.model.City;
import ru.socialnet.team29.model.Country;
import ru.socialnet.team29.model.Person;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PersonService {
    private final PersonRepository personRepository;

    public Person getPersonByEmail(String email) {
        List<PersonRecord> persons = personRepository.findPersonByEmail(email);//TODO Здесь должен использоваться mapstruct для маппинга

        if (persons.size() != 0) {
            return mappingPersonRecordToPerson(persons.get(0));
        } else {
            throw new NoDataFoundException("No users found such email");
        }
    }

    private Person mappingPersonRecordToPerson(PersonRecord personRecord) {
        return Person.builder()
                .id(personRecord.getId())
                .firstName(personRecord.getFirstName())
                .lastName(personRecord.getLastName())
                .password(personRecord.getPassword())
//                .registrationDate(personRecord.getRegDate()) /** Не сходятся в БД ЛокалДэте, а Персон - ЛокалСтэмп
//                .birthDate(System.currentTimeMillis())
                .city(new City(1L, "Moscow"))
                .country(new Country(1L, "Russia"))
                .email(personRecord.getEMail())
                .phone(personRecord.getPhone())
                .confirmationCode(personRecord.getConfirmationCode())
                .photo(personRecord.getPhoto())
                .about(personRecord.getAbout())
                .build();

    }
}
