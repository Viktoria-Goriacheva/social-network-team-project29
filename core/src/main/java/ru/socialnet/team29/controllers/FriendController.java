package ru.socialnet.team29.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.socialnet.team29.answers.AnswerListFriendsForPerson;
import ru.socialnet.team29.dto.FriendSearchDto;
import ru.socialnet.team29.dto.PersonSearchDto;
import ru.socialnet.team29.dto.RecommendationFriendsDto;
import ru.socialnet.team29.model.FriendForFront;
import ru.socialnet.team29.service.FriendServiceImpl;

import java.util.HashMap;
import java.util.List;


@RestController
@RequestMapping("/api/v1")
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
    public ResponseEntity<AnswerListFriendsForPerson<FriendForFront>> getFriendsByIdPerson(@RequestParam HashMap<String,String> params) {
        return new ResponseEntity<>(friendService.getAllFriendsForPerson(params), HttpStatus.OK) ;
    }

    /**
     * Получить количество запросов на дружбу
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

    /**
     * Получение всех Id друзей
     */
    @GetMapping("/friends/friendId")
    public ResponseEntity<FriendSearchDto>  getAllFriendIds() {
        return new ResponseEntity<>(friendService.getAllFriendIds(), HttpStatus.OK);
    }

    /**
     * Подписка на участника соц. сети
     */
    @PostMapping("/friends/subscribe/{id}")
    public ResponseEntity<Void> toSubscribe(@PathVariable(value = "id") Integer friendId) {
        return new ResponseEntity<>(friendService.toSubscribe(friendId));
    }

    /**
     * Рекомендации
     */
    @GetMapping("/friends/recommendations")
    public ResponseEntity<List<RecommendationFriendsDto>> getRecommendations() {
        return new ResponseEntity<>(friendService.getRecommendations(), HttpStatus.OK);
    }

    /**
     * Блокировка друга
     * @param friendId идентификатор друга
     */
    @PutMapping("/friends/block/{id}")
    public ResponseEntity<Void> blockFriend(@PathVariable(value = "id") Integer friendId){
        return new ResponseEntity<>(friendService.blockFriend(friendId));
    }

    /**
     * Получение id заблокированных друзей
     */
    @GetMapping("/friends/blockFriendId")
    public ResponseEntity<FriendSearchDto> getIdsBlockedFriends() {
        return new ResponseEntity<>(friendService.getIdsBlockedFriends(), HttpStatus.OK);
    }
}
