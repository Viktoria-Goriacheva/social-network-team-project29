package ru.socialnet.team29.answers;

import org.springframework.stereotype.Component;
import ru.socialnet.team29.model.Person;
import ru.socialnet.team29.serviceInterface.DBConnectionFeignInterface;

@Component
public class FeignAnswerFromDb implements DBConnectionFeignInterface {

    @Override
    public Person savePerson(Person person) {
        return null;
    }
}
