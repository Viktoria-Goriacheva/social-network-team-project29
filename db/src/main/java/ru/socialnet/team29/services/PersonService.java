package ru.socialnet.team29.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jooq.Condition;
import org.jooq.exception.NoDataFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.PageRequest;
import ru.socialnet.team29.dto.PersonSearchDto;
import ru.socialnet.team29.interfaceDb.PersonInterfaceDB;
import ru.socialnet.team29.payloads.AccountSearchFilter;
import ru.socialnet.team29.payloads.AccountSearchPayload;
import ru.socialnet.team29.repository.PersonRepository;
import ru.socialnet.team29.domain.tables.records.PersonRecord;
import ru.socialnet.team29.mappers.PersonMapper;
import ru.socialnet.team29.model.Person;

import java.time.OffsetDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PersonService implements PersonInterfaceDB {
    private final PersonRepository personRepository;
    private final PersonMapper personMapper;

    @Override
    public Person getPersonByEmail(String email) {
        PersonRecord person = personRepository.findPersonByEmail(email);
        if (person != null) { // убрал проверку isLegal
            return personMapper.PersonRecordToPerson(person);
        } else {
            throw new NoDataFoundException("No users found such email");
        }
    }

    private boolean isLegalPerson(PersonRecord person) {
        return !isDeletedPerson(person) && !isBlockedPerson(person);
    }

    private boolean isBlockedPerson(PersonRecord person) {
        return person.getIsBlocked() != null && person.getIsBlocked();
    }

    private boolean isDeletedPerson(PersonRecord person) {
        return person.getIsDeleted() != null && person.getIsDeleted();
    }

    public Person getPersonByToken(String token) {
        PersonRecord person = personRepository.findPersonByToken(token);
        if (person != null && isLegalPerson(person)) {
            return personMapper.PersonRecordToPerson(person);
        } else {
            throw new NoDataFoundException("No users found such token");
        }
    }

    @Override
    public String findEmailByPersonId(String id) {
        return String.valueOf(personRepository.findEmailByPersonId(id));
    }


    /**
     * @deprecated Вместо этого метода предлагаются более подходящие методы </br>
     * {@link PersonService#isExist(int)},
     * {@link PersonService#findByIdList(List)}
     * @param condition условие выборки
     * @return список аккаунтов
     */
    @Deprecated
    public List<PersonRecord> findAll(Condition condition) {
        return personRepository.findAll(condition);
    }

    public int savePerson(Person person) {
        log.info("Сохраняем новый аккаунт {}", person.getEmail());
        return personRepository.insert(personMapper.PersonToPersonRecord(person));
    }

    public Person update(Person person) {
        log.info("Обновляем данные аккаунта {}", person.getId());
        PersonRecord personRecord = personRepository.update(personMapper.PersonToPersonRecord(person));
        return personMapper.PersonRecordToPerson(personRecord);
    }

    public boolean delete(int id) {
        log.info("Удаляем аккаунт id={}", id);
        return personRepository.delete(id);
    }

    public Person findById(int id) {
        log.info("Запрос данных аккаунта id={}", id);
        PersonRecord person = personRepository.findById(id);
        if (person != null && isLegalPerson(person)) {
            var result = personMapper.PersonRecordToPerson(personRepository.findById(id));
             // todo добавить friendService.getFriendshipStatus(myId, personId);
            // вот только откуда брать свой собственный id???????
            // наверное нужно передавать из Core ещё и свой id.
//            result.setStatusCode(friendService.getFriendshipStatus(myId, id).getValue());
            return result;
        }
        log.info("Аккаунт не найден, либо удален или заблокирован");
        return null;
    }

    public PageImpl<Person> findByPageRequest(PageRequest pageRequest) {
        log.info("Запрос списка всех аккаунтов постранично: {}", pageRequest);
        List<Person> content = personMapper.PersonRecordsToPersons(personRepository.findByPageRequest(pageRequest));
        int total = personRepository.count();
        return new PageImpl<>(content, pageRequest, total);
    }

    // todo метод требует проверки от frontend
    @Deprecated
    public Page<Person> findByFilter(AccountSearchFilter searchFilter) {
        log.info("Запрос списка аккаунтов по фильтру поиска");
        List<Person> content = personMapper.PersonRecordsToPersons(
//          personRepository.findBySearchFilter(searchFilter)
                findByIdList(getAllPersonIds())
        );
        PageRequest pageRequest = PageRequest.of(searchFilter.getPageNumber(), searchFilter.getPageSize());
        long total = 100;
        total = personRepository.count();
        return new PageImpl<>(content, pageRequest, total);
    }

    public List<PersonRecord> findByIdList(List<Integer> ids) {
        log.info("Запрос списка аккаунтов {}", ids);
        return personRepository.findByIds(ids);
    }

    public List<PersonRecord> findByIdListAndFilter(List<Integer> ids, PersonSearchDto filter) {
        log.info("Запрос списка аккаунтов {} по фильтру", ids);
        return personRepository.findByIdListAndFilter(ids, filter);
    }

    public boolean isExist(int id) {
        log.info("Запрос на существование профиля id={}", id);
        return findById(id) != null;
    }

    public void setOnline(String email) {
        var person = getPersonByEmail(email);
        log.info("Пользователь {} онлайн", person.getEmail());
        person.setIsOnline(true);
        person.setLastOnlineTime(OffsetDateTime.now());
        personRepository.update(personMapper.PersonToPersonRecord(person));
    }

    public void setOffline(int id) {
        var person = personRepository.findById(id);
        log.info("Пользователь {} оффлайн", person.getEmail());
        person.setIsOnline(false);
        person.setLastOnlineTime(OffsetDateTime.now());
        personRepository.update(person);
    }

    public boolean isRegisteredMail(String email) {
        return personRepository.findPersonByEmail(email) != null;
    }

    public Page<Person> getPersonsByIds(List<Integer> ids, PageRequest pageRequest) {
        List<Person> content = personMapper.PersonRecordsToPersons(personRepository.findPageByIds(ids, pageRequest));
        long total = personRepository.findByIds(ids).size();
        return new PageImpl<>(content, pageRequest, total);
    }

    public List<Integer> getAllPersonIds() {
        return personRepository.findAllIds();
    }

    public PageImpl<Person> findBySearchPayload(AccountSearchPayload searchPayload) {
        log.info("Запрос списка аккаунтов по параметрам поиска");
        Condition condition = getConditionsFromSearchPayload(searchPayload);
        PageRequest pageRequest = PageRequest.of(searchPayload.getPage(), searchPayload.getSize());
        List<Person> content = personMapper.PersonRecordsToPersons(personRepository.findByCondition(condition, pageRequest));
        var totalCount = personRepository.countByCondition(condition);
        return new PageImpl<>(content, pageRequest, totalCount);
    }

    private Condition getConditionsFromSearchPayload(AccountSearchPayload searchPayload) {
        Condition condition = (ru.socialnet.team29.domain.tables.Person.PERSON.IS_DELETED.isNull());
        condition = condition.or(ru.socialnet.team29.domain.tables.Person.PERSON.IS_DELETED.isFalse());
        String author = searchPayload.getAuthor();
        String firstName = (author.isEmpty() ? searchPayload.getFirstName() : author);
        String lastName = searchPayload.getLastName();
        String country = searchPayload.getCountry();
        String city = searchPayload.getCity();
        log.info("Параметры поиска: Имя = '{}', Фамилия = '{}', Страна = '{}', Город = '{}'", firstName, lastName, country, city);
        if (!firstName.isEmpty())
            condition = condition.and(ru.socialnet.team29.domain.tables.Person.PERSON.FIRST_NAME.containsIgnoreCase(firstName));
        if (!lastName.isEmpty())
            condition = condition.and(ru.socialnet.team29.domain.tables.Person.PERSON.LAST_NAME.containsIgnoreCase(lastName));
        if (!country.isEmpty())
            condition = condition.and(ru.socialnet.team29.domain.tables.Person.PERSON.COUNTRY.containsIgnoreCase(searchPayload.getCountry()));
        if (!city.isEmpty())
            condition = condition.and(ru.socialnet.team29.domain.tables.Person.PERSON.CITY.containsIgnoreCase(city));

        if ((searchPayload.getAgeFrom() != 0) || (searchPayload.getAgeTo() != 0)) {
            OffsetDateTime bdFrom = OffsetDateTime.now().minusYears(searchPayload.getAgeTo());
            OffsetDateTime bdTo = OffsetDateTime.now().minusYears(searchPayload.getAgeFrom());
            log.info("Интервал возраста: {}, {}", bdFrom, bdTo);
            condition = condition.and(ru.socialnet.team29.domain.tables.Person.PERSON.BIRTH_DATE.between(bdFrom, bdTo));
        }
        return condition;
    }
}
