package ru.socialnet.team29.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.socialnet.team29.answers.AnswerListFriendsForPerson;
import ru.socialnet.team29.service.FriendServiceImpl;

import java.util.HashMap;


@RestController
@RequestMapping("/api/v1")
@Slf4j
@RequiredArgsConstructor
public class FriendController {

    private final FriendServiceImpl friendService;

    /**
     * Добавление друга
     */
    @PostMapping("/friends/{id}/request")
    public ResponseEntity<Void> addFriendRequest(@PathVariable(value = "id") Integer friendId) {
        return new ResponseEntity<>(friendService.addFriendRequest(friendId));
    }

    /**
     * Подтверждение дружбы
     */
    @PutMapping("/friends/{id}/approve")
    public ResponseEntity<Void> approveFriendship(@PathVariable(value = "id") Integer friendId) {
        return new ResponseEntity<>(friendService.approveFriendship(friendId));
    }

    /**
     * Получение всех друзей
     */
    @GetMapping("/friends")
    public ResponseEntity<AnswerListFriendsForPerson> getFriendsByIdPerson(@RequestParam HashMap<String,String> params) {
        return new ResponseEntity<>(friendService.getAllFriendsForPerson(params), HttpStatus.OK) ;
    }

    /**
     * Получить количество друзей
     */
    @GetMapping("/friends/count")
    public String friendsCount() {
        return String.valueOf(friendService.getCountOfFriends());
    }

    /**
     * Имеется ли друг с id
     */
    @GetMapping("/friends/{id}")
    public ResponseEntity<Void> friendsByIdExists(@PathVariable(value = "id") Integer friendId) {
        return new ResponseEntity<>(friendService.friendsByIdExists(friendId));
    }

    /**
     * Удаление друга
     */
    @DeleteMapping("/friends/{id}")
    public ResponseEntity<Void> deleteFriendship(@PathVariable(value = "id") Integer friendId) {
        return new ResponseEntity<>(friendService.deleteFriendship(friendId));
    }
}
