package ru.socialnet.team29.serviceInterface;


import org.springframework.data.domain.Pageable;
import ru.socialnet.team29.model.PageableObject;
import ru.socialnet.team29.model.Person;
import ru.socialnet.team29.payloads.AccountUpdatePayload;

public interface PersonService
{

    Integer getIdPersonFromSecurityContext();

    Integer getMyId();
    Person findByEmail(String email);
    Person findMe();
    Person updateMe(AccountUpdatePayload payload);
    String deleteMe();
    Person findById(Integer id);
    PageableObject findAll(Pageable pageable);
    Integer saveNewProfile(Person person);

    void setOnLine(String email);

    void setOffLine(int id);
}
