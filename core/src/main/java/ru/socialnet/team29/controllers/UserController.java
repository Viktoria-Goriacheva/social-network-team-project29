package ru.socialnet.team29.controllers;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.socialnet.team29.exception.ErrorResponse;
import ru.socialnet.team29.model.Person;
import ru.socialnet.team29.service.PersonServiceImpl;

@Controller
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {
    private final PersonServiceImpl personService;

    @GetMapping("/v1/account/me")
    @ApiOperation(value = "Получение текущего пользователя")
    @ApiResponses({@ApiResponse(code = 200, message = "Успешное получение текущего пользователя"),
            @ApiResponse(code = 401, message = "unauthorized", response = ErrorResponse.class)})
    @ResponseBody
    public Person getProfile()  {
        return personService.createNewTestPerson();

    }
}
