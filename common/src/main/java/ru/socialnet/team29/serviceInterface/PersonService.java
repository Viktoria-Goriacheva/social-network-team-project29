package ru.socialnet.team29.serviceInterface;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import ru.socialnet.team29.model.Person;
import ru.socialnet.team29.payloads.AccountSearchFilter;
import ru.socialnet.team29.payloads.AccountSearchPayload;
import ru.socialnet.team29.payloads.AccountUpdatePayload;
import ru.socialnet.team29.responses.RestPageImpl;

import java.util.List;

public interface PersonService
{

    Integer getIdPersonFromSecurityContext();

    Integer getMyId();
    Person findByEmail(String email);
    Person findMe();
    Person updateMe(AccountUpdatePayload payload);
    String deleteMe();
    Person findById(Integer id);
    Page<Person> findAll(PageRequest pageRequest);
    Integer saveNewProfile(Person person);

    void setOnLine(String email);

    void setOffLine(int id);

    List<Integer> findAllIds();

    Page<Person> findAllByIds(Integer[] ids, PageRequest pageRequest);

    Page<Person> searchByFilter(AccountSearchFilter searchFilter);

   RestPageImpl<Person> searchByPayload(AccountSearchPayload searchPayload);
}
