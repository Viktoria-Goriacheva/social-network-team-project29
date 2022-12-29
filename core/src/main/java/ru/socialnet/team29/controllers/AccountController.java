package ru.socialnet.team29.controllers;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.socialnet.team29.answers_interface.CommonAnswer;
import ru.socialnet.team29.model.PageableObject;
import ru.socialnet.team29.model.Person;
import ru.socialnet.team29.payloads.AccountUpdatePayload;
import ru.socialnet.team29.service.EmailService;
import ru.socialnet.team29.payloads.ContactConfirmationPayload;
import ru.socialnet.team29.service.UserDataService;
import ru.socialnet.team29.serviceInterface.PersonService;

@RestController
@RequestMapping("/api/v1/account")
@RequiredArgsConstructor
public class AccountController {
    private final PersonService personService;
    private final EmailService mailService;

    @GetMapping("/me") // ok
    public ResponseEntity<Person> getProfile() {
        return ResponseEntity.ok(personService.findMe());
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

    @GetMapping(value = "/{id}")    // ?
    public ResponseEntity<Person> getProfileById(@RequestParam Integer id) {
        return new ResponseEntity<>(personService.findById(id), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<PageableObject> getAllProfiles(@RequestParam Pageable pageable) {
        return new ResponseEntity<>(personService.findAll(pageable), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Integer> saveNewProfile(@RequestBody Person person) {
        return new ResponseEntity<>(personService.saveNewProfile(person), HttpStatus.OK);
    }
}