package ru.socialnet.team29.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.socialnet.team29.model.Person;
import ru.socialnet.team29.services.PersonService;

@Controller
@RequiredArgsConstructor
@Slf4j
public class PersonController {
    private final PersonService personService;

    @GetMapping("/person")
    @ResponseBody
    public Person getPersonByEmail(@RequestParam("email") String email){
      log.info("Получили запрос от core - Найти персон по email " + email);
        return personService.getPersonByEmail(email);
    }

}
