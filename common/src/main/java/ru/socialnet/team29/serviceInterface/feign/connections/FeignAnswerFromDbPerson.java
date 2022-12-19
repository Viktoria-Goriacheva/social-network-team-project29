package ru.socialnet.team29.serviceInterface.feign.connections;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import ru.socialnet.team29.model.Person;
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
    public Person getPersonById(int id) {
        return null;
    }

    @Override
    public List<Person> getAllPersons(Pageable pageable) {
        return null;
    }

    @Override
    public void setOnLine(String email) {
    }

    @Override
    public void setOffline(int id) {
    }
}
