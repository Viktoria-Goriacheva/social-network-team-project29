package ru.socialnet.team29.security.jwt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import ru.socialnet.team29.answers.MessageAnswer;
import ru.socialnet.team29.dto.PersonLoginDTO;
import ru.socialnet.team29.security.CoreUserDetails;
import ru.socialnet.team29.security.CoreUserDetailsService;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserRegister {

    private final AuthenticationManager authenticationManager;
    private final CoreUserDetailsService coreUserDetailsService;
    private final JWTUtil jwtUtil;


    public MessageAnswer jwtLogin(PersonLoginDTO payload) {
        log.info("Проводим аутентификацию перед выдачей токена " + payload.getEmail());
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(payload.getEmail(), payload.getPassword()));
        log.info(" Аутентификация прошла успешно. " +
                "Теперь запрос в БД для формирования токена по пользователю " + payload.getEmail());
        CoreUserDetails userDetails = (CoreUserDetails) coreUserDetailsService.loadUserByUsername(payload.getEmail());
        String jwtToken = jwtUtil.generateToken(userDetails, payload.getEmail());
        MessageAnswer response = new MessageAnswer();
        response.setMessage(jwtToken);
        log.info("Для " + payload.getEmail() + " выдан токен " + response.getMessage());

        return response;
    }
}


