package ru.socialnet.team29.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.socialnet.team29.model.Person;
import ru.socialnet.team29.service.UserDataService;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
public class CoreUserDetailsService implements UserDetailsService {

    private final UserDataService userDataService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Person person;
        log.info(this.getClass().getSimpleName()+ ": " + "Отправляем запрос на БД " + username);
        person = userDataService.getPersonByEmail(username);
        return new CoreUserDetails(person) {
        };
    }
}
