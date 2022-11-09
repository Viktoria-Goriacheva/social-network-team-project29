package ru.socialnet.team29.serviceInterface;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.socialnet.team29.model.Person;

@FeignClient(name = "db", url = "${server.db.port}")
public interface DBConnectionFeignInterface {

    @PostMapping(value = "/person")
    Person savePerson(@RequestBody Person person);

}
