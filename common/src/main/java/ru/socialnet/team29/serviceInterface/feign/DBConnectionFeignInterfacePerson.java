package ru.socialnet.team29.serviceInterface.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import ru.socialnet.team29.model.Person;
import ru.socialnet.team29.payloads.AccountSearchFilter;
import ru.socialnet.team29.payloads.AccountSearchPayload;
import ru.socialnet.team29.responses.RestPageImpl;

import java.util.List;

@FeignClient(name = "dbPerson", url = "${server.db.port}")

public interface DBConnectionFeignInterfacePerson {

    @PostMapping(value = "/person")
    int savePerson(@RequestBody Person person);

    @GetMapping(value = "/person")
    Person getPersonByEmail(@RequestParam String email);

    @GetMapping(value = "/personToken")
    Person getPersonByToken(@RequestParam String token);

    @PutMapping(value = "/person")
    Person updatePerson(@RequestBody Person person);

    @DeleteMapping(value = "/person")
    Boolean deletePerson(@RequestParam int id);

    @GetMapping(value = "/person/{id}")
    Person getPersonById(@RequestParam Integer meId, @RequestParam Integer id);

    @GetMapping(value = "/persons")
    Page<Person> getAllPersons(@RequestBody PageRequest pageRequest);

    @GetMapping(value = "/person/online")
    void setOnLine(@RequestParam String email);

    @GetMapping(value = "/person/offline")
    void setOffline(@RequestParam int id);

    @GetMapping(value = "/person/exist")
    boolean isRegisteredMail(@RequestParam String email);

    @GetMapping(value = "/person/accountIds")
    Page<Person> getAllPersonsByIds(@RequestParam List<Integer> ids, PageRequest pageRequest);

    @PostMapping(value = "/person/filter")
    Page<Person> getPersonsBySearchFilter(@RequestBody AccountSearchFilter searchFilter);

    @GetMapping(value = "person/ids")
    List<Integer> getAllIds();

    @PostMapping(value = "/person/search")
    RestPageImpl<Person> getPersonsBySearchPayload(@RequestBody AccountSearchPayload searchPayload);
}