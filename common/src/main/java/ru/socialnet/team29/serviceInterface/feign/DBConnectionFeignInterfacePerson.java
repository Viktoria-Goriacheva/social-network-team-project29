package ru.socialnet.team29.serviceInterface.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import ru.socialnet.team29.model.Person;

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
    Person getPersonById(@RequestParam int id);

    @GetMapping(value = "/persons")
    List<Person> getAllPersons(@RequestBody Pageable pageable);

    @PostMapping(value = "/person/online")
    void setOnLine(@RequestParam String email);

    @PostMapping(value = "/person/offline")
    void setOffline(@RequestParam int id);
}