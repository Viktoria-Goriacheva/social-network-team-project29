package ru.socialnet.team29.controllers;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.socialnet.team29.model.Person;
import ru.socialnet.team29.payloads.*;
import ru.socialnet.team29.responses.RestPageImpl;
import ru.socialnet.team29.service.EmailService;
import ru.socialnet.team29.service.FriendServiceImpl;
import ru.socialnet.team29.serviceInterface.PersonService;

import java.util.List;

import java.util.HashMap;

@Slf4j
@RestController
@RequestMapping("/api/v1/account")
@RequiredArgsConstructor
public class AccountController {
    private final PersonService personService;
    private final EmailService mailService;

    @GetMapping("/me") // ok
    public ResponseEntity<Person> getProfile() {
        Person me = personService.findMe();
        if (me == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return ResponseEntity.ok(me);
    }

    @PutMapping(value = "/recovery")
    public ResponseEntity<HttpStatus> recoverPassword(@RequestBody String email) throws MessagingException {
//        mailService.sendEmail("temp", email, "subject");
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping(value = "/me")  // ok
    public ResponseEntity<Person> editProfile(@RequestBody AccountUpdatePayload payload) {
        return new ResponseEntity<>(personService.updateMe(payload), HttpStatus.OK);
    }

    @DeleteMapping(value = "/me")   // ok
    public ResponseEntity<String> deleteProfile() {
        return new ResponseEntity<>(personService.deleteMe(), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")    // ok
    public ResponseEntity<Person> getProfileById(@PathVariable int id) {
        return new ResponseEntity<>(personService.findById(id), HttpStatus.OK);
    }

    @GetMapping // сделано, нужно проверить
    public ResponseEntity<Page<Person>> getAllProfiles(@RequestBody PageRequest pageRequest) {
        return new ResponseEntity<>(personService.findAll(pageRequest), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Integer> saveNewProfile(@RequestBody Person person) {
        return new ResponseEntity<>(personService.saveNewProfile(person), HttpStatus.OK);
    }

    @GetMapping(value = "/search")
    public ResponseEntity<RestPageImpl<Person>> searchProfile(
            @RequestParam(defaultValue = "") String author,
            @RequestParam(defaultValue = "") String firstName,
            @RequestParam(defaultValue = "") String lastName,
            @RequestParam(defaultValue = "") String city,
            @RequestParam(defaultValue = "") String country,
            @RequestParam(defaultValue = "0") int ageTo,
            @RequestParam(defaultValue = "0") int ageFrom,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "0") int page) {
        AccountSearchPayload searchPayload = AccountSearchPayload.builder()
                .author(author)
                .firstName(firstName)
                .lastName(lastName)
                .city(city)
                .country(country)
                .ageFrom(ageFrom)
                .ageTo(ageTo)
                .size(size)
                .page(page)
                .build();
        return new ResponseEntity<>(personService.searchByPayload(searchPayload), HttpStatus.OK);
    }

    @PostMapping(value = "/searchByFilter") // неизвестно, где этот запрос используется, поэтому работает некорректно
    public ResponseEntity<Page<Person>> searchByFilter(@RequestBody AccountSearchFilter searchFilter) {
        return new ResponseEntity<>(personService.searchByFilter(searchFilter), HttpStatus.OK);
    }

    @GetMapping(value = "/ids") // список id всех аккаунтов
    public ResponseEntity<List<Integer>> getProfileIds() {
        return new ResponseEntity<>(personService.findAllIds(), HttpStatus.OK);
    }

    @GetMapping(value = "/accountIds") // сделано, надо проверить
    public ResponseEntity<Page<Person>> getProfilesByIds(@RequestBody Integer[] ids, PageRequest pageRequest) {
        return new ResponseEntity<>(personService.findAllByIds(ids, pageRequest), HttpStatus.OK);
    }
}