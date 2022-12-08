package ru.socialnet.team29.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.socialnet.team29.answers_interface.CommonAnswer;
import ru.socialnet.team29.model.Person;
import ru.socialnet.team29.payloads.ContactConfirmationPayload;
import ru.socialnet.team29.service.UserDataService;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AccountController {
    private final UserDataService userDataService;

    @GetMapping("/v1/account/me")
    public ResponseEntity<Person> getProfile() {
        return ResponseEntity.ok(userDataService.getCurrentAccount());
    }

}

