package ru.socialnet.team29.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.socialnet.team29.model.Person;
import ru.socialnet.team29.payloads.AccountSearchFilter;
import ru.socialnet.team29.payloads.AccountSearchPayload;
import ru.socialnet.team29.payloads.AccountUpdatePayload;
import ru.socialnet.team29.responses.RestPageImpl;
import ru.socialnet.team29.security.CoreUserDetails;
import ru.socialnet.team29.serviceInterface.PersonService;
import ru.socialnet.team29.serviceInterface.feign.DBConnectionFeignInterfacePerson;

import java.time.OffsetDateTime;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService {

    private final DBConnectionFeignInterfacePerson feignInterface;

    public Integer getIdPersonFromSecurityContext() {
        return ((CoreUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getPerson().getId();

    }

    public Person getPersonFromSecurityContext() {
        return ((CoreUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getPerson();

    }


    @Override
    public Person findByEmail(String email){
        log.info("Запрос профиля по email {}", email);
        return feignInterface.getPersonByEmail(email);
    }


    @Override
    public Person findMe() {
        var id = getIdPersonFromSecurityContext();
        var result = findById(id);
        log.info("PersonFromSecurityContext() = {}", result);
        return result;
    }

    @Override
    public Person updateMe(AccountUpdatePayload payload) {
        log.info("Запрос на обновление данных профиля");
        Person me = findMe();
        BeanUtils.copyProperties(payload, me);
        me.setUpdatedOn(OffsetDateTime.now());
        try {
            me.setBirthDate(OffsetDateTime.parse(payload.getBirthDate()));
        } catch (DateTimeParseException ex){
            me.setBirthDate(null);
        }
        return feignInterface.updatePerson(me);
    }

    @Override
    public String deleteMe() {
        log.info("Запрос на удаление моего профиля");
        int myId = getMyId();
        if (myId == 0)
            return "Unauthorized";
        if (feignInterface.deletePerson(myId))
            return "Successfully";
        return "Account Not Found";
    }

    @Override
    public Person findById(Integer id) {
        log.info("Запрос данных профиля id={}", id);
        var meId = getIdPersonFromSecurityContext();
        return feignInterface.getPersonById(meId, id);
    }

    @Override
    public Page<Person> findAll(PageRequest pageRequest) {
        return feignInterface.getAllPersons(pageRequest);
    }

    @Override
    public Integer getMyId() {
        log.info("Запрос моего id");
        int id = getIdPersonFromSecurityContext();
        log.info("id = {}", id);
        return id;
    }

    @Override
    public Integer saveNewProfile(Person person) {
        log.info("Создание нового профиля");
        return feignInterface.savePerson(person);
    }

    @Override
    public void setOnLine(String email) {
        feignInterface.setOnLine(email);
    }

    @Override
    public void setOffLine(int id) {
        feignInterface.setOffline(id);
    }

    @Override
    public List<Integer> findAllIds() {
        return feignInterface.getAllIds();
    }

    @Override
    public Page<Person> findAllByIds(Integer[] ids, PageRequest pageRequest) {
        return feignInterface.getAllPersonsByIds(Arrays.asList(ids), pageRequest);
    }

    @Override
    public Page<Person> searchByFilter(AccountSearchFilter searchFilter) {
        return feignInterface.getPersonsBySearchFilter(searchFilter);
    }

    @Override
    public RestPageImpl<Person> searchByPayload(AccountSearchPayload searchPayload) {
        log.info("запрос списка профилей по параметрам поиска: {}", searchPayload);
        return feignInterface.getPersonsBySearchPayload(searchPayload);
    }
}
