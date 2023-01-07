package ru.socialnet.team29.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.socialnet.team29.model.Person;
import ru.socialnet.team29.services.PersonService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class PersonController {
    private final PersonService personService;

    @GetMapping("/person")
    public Person getPersonByEmail(@RequestParam("email") String email){
        return personService.getPersonByEmail(email);
    }

    @GetMapping("/personToken")
    public Person getPersonByToken(@RequestParam("token") String token){
        return personService.getPersonByToken(token);
    }

    @PostMapping("/person")
    public int savePerson(@RequestBody Person person) {
        return personService.savePerson(person);
    }

    @PutMapping("/person")
    public Person updatePerson(@RequestBody Person person) {
        return personService.update(person);
    }

    @DeleteMapping("/person")
    public boolean deletePerson(@RequestParam int id) {
        return personService.delete(id);
    }

    @GetMapping("/person/{id}")
    public Person getPersonById(@RequestParam int id) {
        return personService.findById(id);
    }

    @GetMapping("/persons")
    // параметр запроса
    public List<Person> getAllPersons(@RequestBody Pageable pageable) {
        return personService.findByPageableTerm(pageable);
    }

    @GetMapping("/person/online")
    public void setOnline(@RequestParam String email) {
        personService.setOnline(email);
    }

    @GetMapping("/person/offline")
    public void setOffline(@RequestParam int id) {
        personService.setOffline(id);
    }

    @GetMapping(value = "/person/exist")
    boolean isRegisteredMail(@RequestParam String email) {
        return personService.isRegisteredMail(email);
    }
}
