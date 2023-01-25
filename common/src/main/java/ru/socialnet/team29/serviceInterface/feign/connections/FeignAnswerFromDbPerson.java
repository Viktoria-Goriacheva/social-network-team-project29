package ru.socialnet.team29.serviceInterface.feign.connections;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import ru.socialnet.team29.model.Person;
import ru.socialnet.team29.payloads.AccountSearchFilter;
import ru.socialnet.team29.payloads.AccountSearchPayload;
import ru.socialnet.team29.responses.RestPageImpl;
import ru.socialnet.team29.serviceInterface.feign.DBConnectionFeignInterfacePerson;

import java.util.List;

@Component
public class FeignAnswerFromDbPerson implements DBConnectionFeignInterfacePerson {

    @Override
    public int savePerson(Person person) {
        return 0;
    }

    @Override
    public Person getPersonByEmail(String email) {
        return null;
    }
    @Override
    public Person getPersonByToken(@RequestParam String token) {
        return null;
    };

    @Override
    public Person updatePerson(@RequestBody Person person) {
        return null;
    }

    @Override
    public Boolean deletePerson(int id) {
        return null;
    }

    @Override
    public Person getPersonById(Integer meId, Integer id) {
        return null;
    }

    @Override
    public Page<Person> getAllPersons(PageRequest pageRequest) {
        return null;
    }

    @Override
    public void setOnLine(String email) {
    }

    @Override
    public void setOffline(int id) {
    }

    @Override
    public boolean isRegisteredMail(String email) {
        return false;
    }

    @Override
    public Page<Person> getAllPersonsByIds(List<Integer> ids, PageRequest pageRequest) {
        return null;
    }

    @Override
    public Page<Person> getPersonsBySearchFilter(AccountSearchFilter searchFilter) {
        return null;
    }

    @Override
    public List<Integer> getAllIds() {
        return null;
    }

    @Override
    public RestPageImpl<Person> getPersonsBySearchPayload(AccountSearchPayload searchPayload) {
        return null;
    }
}
