package ru.socialnet.team29.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.socialnet.team29.model.PageableObject;
import ru.socialnet.team29.model.Person;
import ru.socialnet.team29.payloads.AccountUpdatePayload;
import ru.socialnet.team29.security.CoreUserDetails;
import ru.socialnet.team29.serviceInterface.PersonService;
import ru.socialnet.team29.serviceInterface.feign.DBConnectionFeignInterfacePerson;

import java.time.OffsetDateTime;
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
        log.info("Запрос данных моего профиля");
        int myId = getMyId();
        return feignInterface.getPersonById(myId);
    }

    @Override
    public Person updateMe(AccountUpdatePayload payload) {
        log.info("Запрос на обновление данных профиля");
        Person me = mergePersonAndPayload(findMe(), payload);
        me.setUpdatedOn(OffsetDateTime.now());
        return feignInterface.updatePerson(me);
    }

    private Person mergePersonAndPayload(Person me, AccountUpdatePayload payload) {
        me.setFirstName(payload.getFirstName());
        me.setLastName(payload.getLastName());
        me.setBirthDate(payload.getBirthDate());
        me.setAbout(payload.getAbout());
        me.setCountry(payload.getCountry());
        me.setCity(payload.getCity());
        me.setPhone(payload.getPhone());
        me.setPhoto(payload.getPhoto());
        return me;
    }

    @Override
    public String deleteMe() {
        log.info("Запрос на удаление моего профиля");
        int myId = getMyId();
        if (myId == 0)
            return "Unauthorized";
        if (feignInterface.deletePerson(getMyId()))
            return "Successfully";
        return "Account Not Found"; //todo throw Exceptions
    }

    @Override
    public Person findById(Integer id) {
        log.info("Запрос данных профиля id={}", id);
        return feignInterface.getPersonById(id);
    }

    @Override
    public PageableObject findAll(Pageable pageable) {
        List<Person> allPersons = feignInterface.getAllPersons(pageable);
        return new PageableObject();
    }

    @Override
    public Integer getMyId() {
        log.info("Запрос моего id");
        int id = 0;
//        if (SecurityContextHolder.getContext().getAuthentication().isAuthenticated())
        id = ((CoreUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getPerson().getId();
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
}
