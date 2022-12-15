package ru.socialnet.team29.serviceInterface;

import org.springframework.http.HttpStatus;
import ru.socialnet.team29.answers.AnswerListFriendsForPerson;

import java.util.Map;

public interface FriendService {
    /**
     * Запрос на добавление друга
     * @param friendId идентификатор друга
     * @return true - в случае успеха
     */
    HttpStatus addFriendRequest(Integer friendId);

    /**
     * Подтверждение дружбы
     * @param friendId идентификатор друга
     * @return true - в случае успеха
     */
    HttpStatus approveFriendship(Integer friendId);

    /**
     * Получить всех друзей персоны
     * @param params набор параметров
     * @return список друзей класса FriendForFront
     */
    AnswerListFriendsForPerson getAllFriendsForPerson(Map<String, String> params);

    /**
     * Удалдение друга
     * @param friendId идентификатор друга
     * @return true - в случае успеха
     */
    HttpStatus deleteFriendship(Integer friendId);

    /**
     * Проверка друга
     * @param friendId идентификатор друга
     * @return true - в случае успеха
     */
    HttpStatus friendsByIdExists(Integer friendId);

    /**
     * Получить количество друзей
     * @return количество друзей
     */
    Integer getCountOfFriends();
}
