package ru.socialnet.team29.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.socialnet.team29.model.Person;
import ru.socialnet.team29.serviceInterface.PersonService;

@Service
@RequiredArgsConstructor
@Slf4j
public class CoreUserDetailsService implements UserDetailsService {

    private final PersonService personService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info(this.getClass().getSimpleName()+ ": " + "Отправляем запрос на БД " + username);
        Person person = personService.findByEmail(username);
        return new CoreUserDetails(person) {
        };
    }
}
