package ru.socialnet.team29.controllers;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.socialnet.team29.answers_interface.CommonAnswer;
import ru.socialnet.team29.model.Person;
import ru.socialnet.team29.service.EmailService;
import ru.socialnet.team29.payloads.ContactConfirmationPayload;
import ru.socialnet.team29.service.UserDataService;

@RestController
@RequestMapping("/api/v1/account")
@RequiredArgsConstructor
public class AccountController {
    private final UserDataService userDataService;
    private final EmailService mailService;

    @GetMapping("/me")
    public ResponseEntity<Person> getProfile() {
        return ResponseEntity.ok(userDataService.getCurrentAccount());
    }

    @PutMapping(value = "/recovery")
    public ResponseEntity<HttpStatus> recoverPassword(@RequestBody String email) throws MessagingException {
//        mailService.sendEmail("temp", email, "subject");
        return new ResponseEntity<>(HttpStatus.OK);
    }
}