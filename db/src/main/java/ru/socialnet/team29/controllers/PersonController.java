package ru.socialnet.team29.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.socialnet.team29.model.Person;
import ru.socialnet.team29.payloads.AccountSearchFilter;
import ru.socialnet.team29.payloads.AccountSearchPayload;
import ru.socialnet.team29.services.FriendService;
import ru.socialnet.team29.services.PersonService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class PersonController {
    private final PersonService personService;
    private final FriendService friendService;

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
    public Person getPersonById(@RequestParam Integer meId, @RequestParam Integer id) {
        return personService.findById(meId, id);
    }

    @GetMapping("/persons")
    // параметр запроса
    public Page<Person> getAllPersons(@RequestBody PageRequest pageRequest) {
        return personService.findByPageRequest(pageRequest);
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

    @GetMapping(value = "/person/accountIds")
    Page<Person> getAllPersonsByIds(@RequestBody List<Integer> ids, PageRequest pageRequest) {
        return personService.getPersonsByIds(ids, pageRequest);
    }

    @PostMapping(value = "/person/filter")
    Page<Person> getPersonsBySearchFilter(AccountSearchFilter searchFilter) {
        return personService.findByFilter(searchFilter);
    }

    @GetMapping(value = "person/ids")
    List<Integer> getAllPersonIds() {
        return personService.getAllPersonIds();
    }

    @PostMapping(value = "/person/search")
    PageImpl<Person> getPersonsBySearchPayload(@RequestBody AccountSearchPayload searchPayload){
        return personService.findBySearchPayload(searchPayload);
    }

    @GetMapping(value = "/friends/integer/id")
    List<Integer> getListIdsAllFriendsCurrentUser(@RequestParam Integer idCurrentUser){
     return friendService.getIdsFriendsById(idCurrentUser);
    }

}
