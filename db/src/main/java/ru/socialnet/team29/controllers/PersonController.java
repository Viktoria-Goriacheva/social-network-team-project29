package ru.socialnet.team29.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import ru.socialnet.team29.model.Person;
import ru.socialnet.team29.services.PersonService;

@RestController
@RequiredArgsConstructor
@Slf4j
public class PersonController {
    private final PersonService personService;

    @GetMapping("/person")
    public Person getPersonByEmail(@RequestParam("email") String email){
      log.info("Получили запрос от core - Найти персон по email " + email);
        return personService.getPersonByEmail(email);
    }

    @GetMapping("/personToken")
    public Person getPersonByToken(@RequestParam("token") String token){
        log.info("Получили запрос от core - Найти персон по token " + token);
        return personService.getPersonByToken(token);
    }

    @PostMapping("/person")
    public ResponseEntity<Person> savePerson(@RequestBody Person person) {
        log.info("Получили запрос на сохранение {}",person);
        return new ResponseEntity<>( personService.savePerson(person), HttpStatus.OK);
    }

    @PostMapping("/personUpdate")
    public ResponseEntity<Person> updatePerson(@RequestBody Person person) {
        log.info("Получили запрос на обновление данных! {}",person);
        return new ResponseEntity<>( personService.updatePerson(person), HttpStatus.OK);
    }

}
