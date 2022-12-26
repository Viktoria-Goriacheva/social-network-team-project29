package ru.socialnet.team29.serviceInterface.feign;

import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import ru.socialnet.team29.answers.AnswerListFriendsForPerson;
import ru.socialnet.team29.dto.FriendSearchDto;
import ru.socialnet.team29.model.FriendForFront;
import ru.socialnet.team29.model.Person;
import ru.socialnet.team29.model.PostDto;

@FeignClient(name = "db", url = "${server.db.port}")
public interface DBConnectionFeignInterface {
    @PostMapping(value = "/person")
    Person savePerson(@RequestBody Person person);

    @GetMapping(value = "/person")
    Person getPersonByEmail(@RequestParam String email);

    @GetMapping(value = "/personToken")
    Person getPersonByToken(@RequestParam String token);


    @PostMapping(value = "/personUpdate")
    Person updatePerson(@RequestBody Person person);

    @GetMapping(value = "/posts")
    List<PostDto> getPostDto(@RequestParam String email);

    @GetMapping("/friends/request")
    Boolean addFriendRequest(@RequestParam Integer id, @RequestParam Integer friendId);

    @PutMapping("/friends/approve")
    Boolean approveFriendship(@RequestParam Integer id, @RequestParam Integer friendId);

    @PostMapping("/friends")
    AnswerListFriendsForPerson<FriendForFront> getFriendsByIdPerson(
            @RequestParam Integer id,
            @RequestParam String statusName,
            @RequestBody AnswerListFriendsForPerson.FriendPageable pageable);

    @DeleteMapping("/friends")
    Boolean deleteFriendship(@RequestParam Integer id, @RequestParam Integer friendId);

    @GetMapping("/friends/exists")
    Boolean friendsByIdExists(@RequestParam Integer id, @RequestParam Integer friendId);

    @GetMapping("/friends/count")
    Integer getCountOfFriends(@RequestParam Integer id);

    @GetMapping("/friends/friendId")
    FriendSearchDto getAllFriendIds(@RequestParam Integer id);

    @PostMapping("/friends/subscribe")
    Boolean toSubscribe(@RequestParam Integer id, @RequestParam Integer friendId);

    @PostMapping(value = "/post")
    Boolean savePost (@RequestBody PostDto postDto);

    @GetMapping(value = "/post")
    PostDto getPostById(@RequestParam Integer id);

    @PutMapping(value = "/post")
    Boolean updatePost (@RequestBody PostDto postDto);

    @DeleteMapping(value = "/post")
    Boolean deletePost (@RequestParam Integer id);
}