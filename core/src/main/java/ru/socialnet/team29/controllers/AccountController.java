package ru.socialnet.team29.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.socialnet.team29.answers_interface.CommonAnswer;
import ru.socialnet.team29.payloads.ContactConfirmationPayload;
import ru.socialnet.team29.service.UserDataService;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AccountController {
    private final UserDataService userDataService;

    @PostMapping("/v1/auth/register")
    @ResponseBody
    public ResponseEntity<CommonAnswer> handlerRegisterNewUser(@RequestBody ContactConfirmationPayload contactConfirmationPayload) {
        return new ResponseEntity<>( userDataService.saveNewUserInDb(contactConfirmationPayload), HttpStatus.OK);
    }

}

