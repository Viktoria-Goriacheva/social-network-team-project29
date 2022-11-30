package ru.socialnet.team29.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.socialnet.team29.model.Person;
import ru.socialnet.team29.service.UserDataService;

@RestController
@RequestMapping("/api/v1/account")
@RequiredArgsConstructor
public class AccountController {
    private final UserDataService userDataService;


    @GetMapping("/me")
    public ResponseEntity<Person> getProfile() {
        return ResponseEntity.ok(userDataService.getCurrentAccount());
    }

}

