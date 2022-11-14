package ru.socialnet.team29.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.socialnet.team29.answers_interface.CommonAnswer;
import ru.socialnet.team29.payloads.ContactConfirmationPayload;
import ru.socialnet.team29.service.UserDataService;

@Controller
@RequestMapping("/api")
@RequiredArgsConstructor
public class AccountController {
    private final UserDataService userDataService;

    @PostMapping("/v1/account/register")
    @ResponseBody
    public CommonAnswer handlerRegisterNewUser(@RequestBody ContactConfirmationPayload contactConfirmationPayload) {
        return userDataService.saveNewUserInDb(contactConfirmationPayload);
    }

}

