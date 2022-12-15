package ru.socialnet.team29.serviceInterface.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import ru.socialnet.team29.answers.AnswerListFriendsForPerson;
import ru.socialnet.team29.model.Person;
import ru.socialnet.team29.model.PostDto;

import java.util.List;

@FeignClient(name = "db", url = "${server.db.port}")
public interface DBConnectionFeignInterface {

    @PostMapping(value = "/person")
    Person savePerson(@RequestBody Person person);

    @GetMapping(value = "/person")
    Person getPersonByEmail(@RequestParam String email);

    @GetMapping(value = "/posts")
    List<PostDto> getPostDto(@RequestParam String email);

    @GetMapping("/friends/request")
    Boolean addFriendRequest(@RequestParam Integer id, @RequestParam Integer friendId);

    @PutMapping("/friends/approve")
    Boolean approveFriendship(@RequestParam Integer id, @RequestParam Integer friendId);

    @PostMapping("/friends")
    AnswerListFriendsForPerson getFriendsByIdPerson(
            @RequestParam Integer id,
            @RequestParam String statusName,
            @RequestBody AnswerListFriendsForPerson.FriendPageable pageable);

    @DeleteMapping("/friends")
    Boolean deleteFriendship(@RequestParam Integer id, @RequestParam Integer friendId);

    @GetMapping("/friends/exists")
    Boolean friendsByIdExists(@RequestParam Integer id, @RequestParam Integer friendId);

    @GetMapping("/friends/count")
    Integer getCountOfFriends(@RequestParam Integer id);
}