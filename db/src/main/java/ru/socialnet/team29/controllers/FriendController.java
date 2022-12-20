package ru.socialnet.team29.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.socialnet.team29.answers.AnswerListFriendsForPerson;
import ru.socialnet.team29.dto.FriendSearchDto;
import ru.socialnet.team29.model.FriendForFront;
import ru.socialnet.team29.services.FriendService;

@RestController
@Slf4j
@RequiredArgsConstructor
public class FriendController {
    private final FriendService friendService;

    @GetMapping("/friends/request")
    Boolean addFriendRequest(@RequestParam Integer id, @RequestParam Integer friendId) {
        log.info("Получили запрос от core - Добавить друга => " + friendId);
        return friendService.addFriendRequest(id, friendId);
    }

    @PutMapping("/friends/approve")
    Boolean approveFriendship(@RequestParam Integer id, @RequestParam Integer friendId) {
        log.info("Получили запрос от core - Одобрить друга => " + friendId);
        return friendService.approveFriendship(id, friendId);
    }

    @PostMapping("/friends")
    AnswerListFriendsForPerson<FriendForFront> getFriendsByIdPerson(@RequestParam Integer id,
                                                                    @RequestParam String statusName,
                                                                    @RequestBody AnswerListFriendsForPerson.FriendPageable pageable) {
        log.info("Получили запрос от core - получить всех друзей => " + id.toString() + " со статусом " + statusName);
        return friendService.getFriendsByIdPerson(id, statusName, pageable);
    }

    @GetMapping("/friends/exists")
    Boolean friendsByIdExists(@RequestParam Integer id, @RequestParam Integer friendId) {
        log.info("Получили запрос от core - проверка друга друзей id=" + friendId);
        return friendService.friendsByIdExists(id, friendId);
    }

    @DeleteMapping("/friends")
    Boolean deleteFriendship(@RequestParam Integer id, @RequestParam Integer friendId) {
        log.info("Получили запрос от core - удаление друга id=" + friendId);
        return friendService.deleteFriend(id, friendId);
    }

    @GetMapping("/friends/count")
    Integer getCountOfFriends(@RequestParam Integer id) {
        log.info("Получили запрос от core - на количество запросов на дружбу id=" + id);
        return friendService.getCountOfRequestFrom(id);
    }

    @GetMapping("/friends/friendId")
    FriendSearchDto getAllFriendIds(@RequestParam Integer id) {
        log.info("Получили запрос от core - на выдачу всех id друзей id=" + id);
        return friendService.getAllFriendIds(id);
    };

    @PostMapping("/friends/subscribe")
    Boolean toSubscribe(@RequestParam Integer id, @RequestParam Integer friendId) {
        log.info("Получили запрос от core - подписка на наблюдаемого id=" + friendId);
        return friendService.toSubscribe(id, friendId);
    };
}
