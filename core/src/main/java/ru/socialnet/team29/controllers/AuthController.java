package ru.socialnet.team29.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import ru.socialnet.team29.answers.AnswerWithTwoTokens;
import ru.socialnet.team29.answers.MessageAnswer;
import ru.socialnet.team29.answers.ResponseUserRegister;
import ru.socialnet.team29.answers_interface.CommonAnswer;
import ru.socialnet.team29.dto.PersonLoginDTO;
import ru.socialnet.team29.payloads.ContactConfirmationPayload;
import ru.socialnet.team29.responses.CaptchaResponse;
import ru.socialnet.team29.responses.EmailResponse;
import ru.socialnet.team29.responses.PasswordResponse;
import ru.socialnet.team29.security.jwt.UserRegister;
import ru.socialnet.team29.service.CaptchaService;
import ru.socialnet.team29.service.EmailService;
import ru.socialnet.team29.service.UserDataService;
import ru.socialnet.team29.serviceInterface.LoginService;
import ru.socialnet.team29.serviceInterface.LogoutService;
import ru.socialnet.team29.serviceInterface.PersonService;

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
    private final EmailService mailService;

    private final PersonService personService;


    @PostMapping(value = "/login")
    public ResponseEntity<AnswerWithTwoTokens> loginPage(@RequestBody PersonLoginDTO person,
                                                         HttpServletResponse response) {
        log.info("Попытка входа {}", person.getEmail());
        MessageAnswer answer = userRegister.jwtLogin(person);
        loginService.setCookieToAnswer(response, answer);
        personService.setOnLine(person.getEmail());
        return new ResponseEntity<>(new AnswerWithTwoTokens(answer.getMessage(), ""), HttpStatus.OK);
    }

    @PostMapping(value = "/logout")
    public ResponseUserRegister handlerLogout(HttpServletRequest request) {
        log.info("Попытка выхода  {}", SecurityContextHolder.getContext().getAuthentication().getName());
        personService.setOffLine(personService.getMyId());
        logoutService.logout(request);
        return new ResponseUserRegister("", System.currentTimeMillis(), new MessageAnswer("ok"));
    }

    @PostMapping("/register")
    @ResponseBody
    public ResponseEntity<CommonAnswer> handlerRegisterNewUser(@RequestBody ContactConfirmationPayload contactConfirmationPayload) {
        CommonAnswer answer = userDataService.saveNewUserInDb(contactConfirmationPayload);
        if (answer.getClass()== ResponseUserRegister.class)
            return new ResponseEntity<>(answer, HttpStatus.OK);
        return new ResponseEntity<>(answer, HttpStatus.BAD_REQUEST);
    }

    @GetMapping(value = "/captcha")
    public ResponseEntity<CaptchaResponse> getCaptcha() {
        return ResponseEntity.ok(captchaService.getCaptchaCode());
    }

    @PostMapping(value = "/password/recovery/")
    public ResponseEntity<HttpStatus> recoverPassword(@RequestBody EmailResponse emailResponse) throws Exception {
        log.info(emailResponse.getEmail() + " email from auth controller");

        mailService.generateResetTokenEmail(emailResponse.getEmail());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "/password/recovery/{token}") // данные с формы восстановления пароля (старый/новый пароль)
    public ResponseEntity<HttpStatus> ResetPassword(@PathVariable String token, @RequestBody PasswordResponse password) {
        if (mailService.isTokenExist(token, password)) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @PostMapping(value = "/change-password-link")
    public ResponseEntity<HttpStatus> changePassword() {
        return ResponseEntity.ok(HttpStatus.OK);
    }
    @PostMapping(value = "/change-email-link")
    public ResponseEntity<HttpStatus> getEmail() {
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
