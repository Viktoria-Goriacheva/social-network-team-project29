package ru.socialnet.team29.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.socialnet.team29.answers.AnswerWithTwoTokens;
import ru.socialnet.team29.answers.MessageAnswer;
import ru.socialnet.team29.answers.ResponseUserRegister;
import ru.socialnet.team29.answers_interface.CommonAnswer;
import ru.socialnet.team29.dto.PersonLoginDTO;
import ru.socialnet.team29.payloads.ContactConfirmationPayload;
import ru.socialnet.team29.responses.CaptchaResponse;
import ru.socialnet.team29.security.jwt.UserRegister;
import ru.socialnet.team29.service.CaptchaService;
import ru.socialnet.team29.service.UserDataService;
import ru.socialnet.team29.serviceInterface.LoginService;
import ru.socialnet.team29.serviceInterface.LogoutService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@Slf4j
public class AuthController {

    private final UserRegister userRegister;
    private final LoginService loginService;
    private final LogoutService logoutService;
    private final CaptchaService captchaService;
    private final UserDataService userDataService;

    @PostMapping(value = "/login")
    public ResponseEntity<AnswerWithTwoTokens> loginPage(@RequestBody PersonLoginDTO person,
                                                         HttpServletResponse response) {
        log.info("Попытка входа {}", person.getEmail());
        MessageAnswer answer = userRegister.jwtLogin(person);
        loginService.setCookieToAnswer(response, answer);
        return new ResponseEntity<>(new AnswerWithTwoTokens(answer.getMessage(), ""), HttpStatus.OK);
    }


    @PostMapping(value = "/logout")
    public ResponseUserRegister handlerLogout(HttpServletRequest request) {
        log.info("Попытка выхода  {}", SecurityContextHolder.getContext().getAuthentication().getName());
        logoutService.logout(request);
        return new ResponseUserRegister("", System.currentTimeMillis(), new MessageAnswer("ok"));
    }

    @PostMapping("/register")
    @ResponseBody
    public ResponseEntity<CommonAnswer> handlerRegisterNewUser(@RequestBody ContactConfirmationPayload contactConfirmationPayload) {
        return new ResponseEntity<>(userDataService.saveNewUserInDb(contactConfirmationPayload), HttpStatus.OK);
    }

    @GetMapping(value = "/captcha")
    public ResponseEntity<CaptchaResponse> getCaptcha() {
        return ResponseEntity.ok(captchaService.getCaptchaCode());
    }
}
