package ru.socialnet.team29.controllers;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import ru.socialnet.team29.dto.PersonLoginDTO;
import ru.socialnet.team29.exeption.ErrorResponse;
import ru.socialnet.team29.model.LogoutResponse;
import ru.socialnet.team29.model.Person;
import ru.socialnet.team29.model.ProfileResponse;
import ru.socialnet.team29.serviceInterface.LoginService;
import ru.socialnet.team29.serviceInterface.LogoutService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final LoginService loginService;
    private final LogoutService logoutService;

    @ApiOperation(value = "Вход через логин/пароль")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Успешный вход", response = ProfileResponse.class),
            //ProfileResponse полносью повторяет ответ, ведь не нжно дублировать..
            @ApiResponse(code = 400, message = "Bad Request", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "Server error")
    })
    @PostMapping(value = "/login")
    public ResponseEntity<Person> loginPage(@RequestBody PersonLoginDTO person) {
        ProfileResponse profile = loginService.getProfile(person);
        return new ResponseEntity<>(profile.getPerson(), HttpStatus.OK);
    }

    @ApiOperation(value = "Выход")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Успешный выход", response = ProfileResponse.class),
            //ProfileResponse полносью повторяет ответ, ведь не нжно дублировать..
            @ApiResponse(code = 400, message = "Bad Request", response = ErrorResponse.class)})
    @PostMapping(value = "/logout")
    public ResponseEntity<LogoutResponse> logout() {
        return new ResponseEntity<>(logoutService.getResponse(), HttpStatus.OK);
    }
}
