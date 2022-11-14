package ru.socialnet.team29.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.socialnet.team29.answers.LoginAuthAnswer;
import ru.socialnet.team29.answers.MessageAnswer;
import ru.socialnet.team29.answers.ResponseUserRegister;
import ru.socialnet.team29.dto.PersonLoginDTO;
import ru.socialnet.team29.model.Person;
import ru.socialnet.team29.security.jwt.UserRegister;
import ru.socialnet.team29.service.UserDataService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@Slf4j
public class AuthController {

    private final UserRegister userRegister;
    private final UserDataService userDataService;


    @PostMapping(value = "/login")
    public ResponseEntity<LoginAuthAnswer> loginPage(@RequestBody PersonLoginDTO person,
                                                     HttpServletResponse response) throws IOException {
        log.info(this.getClass().getSimpleName() + ": " + person.getEmail() + " пытается войти.");
        MessageAnswer answer = userRegister.jwtLogin(person);
        Cookie cookie = new Cookie("token", answer.getMessage());
        response.addCookie(cookie);

        log.info(this.getClass().getSimpleName() + ": " + "Посылаем запрос для формирования ответа для фронта по person.");
        Person personFromDb = userDataService.getPersonByEmail(person.getEmail());
        LoginAuthAnswer authAnswer = new LoginAuthAnswer("",
                LocalDateTime.now()
                , personFromDb
                , "ALL"
                , System.currentTimeMillis()
                , false
                , answer.getMessage()
        );


        return new ResponseEntity<>(authAnswer, HttpStatus.OK);
    }

    @PostMapping(value = "/logout")
    @ResponseBody
    public ResponseUserRegister handlerLogout(HttpServletRequest request) {
        HttpSession session = request.getSession();
        SecurityContextHolder.clearContext();
        if (session != null) {
            session.invalidate();
        }
        for (Cookie cookie : request.getCookies()) {
            cookie.setMaxAge(0);
        }
        log.info(this.getClass().getSimpleName() + ": " + "Попытка выхода ");
        return new ResponseUserRegister("", System.currentTimeMillis(), new MessageAnswer("ok"));
    }
}
