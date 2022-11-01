package ru.socialnet.team29.controllers;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.socialnet.team29.exeption.ErrorResponse;
import ru.socialnet.team29.model.ProfileResponse;
import ru.socialnet.team29.service.impl.ProfileServiceImpl;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileServiceImpl profileServiceImpl;

    @GetMapping("/me")
    @ApiOperation(value = "Получение текущего пользователя")
    @ApiResponses({@ApiResponse(code = 200, message = "Успешное получение текущего пользователя"),
                   @ApiResponse(code = 401, message = "unauthorized", response = ErrorResponse.class)})
    public ProfileResponse getProfile(ProfileResponse profileResponse){
        return profileServiceImpl.getProfile(profileResponse);
    }

}

