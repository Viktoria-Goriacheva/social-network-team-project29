package ru.socialnet.team29.serviceInterface;

import org.springframework.http.HttpStatus;
import ru.socialnet.team29.answers.AnswerListFriendsForPerson;
import ru.socialnet.team29.dto.FriendSearchDto;
import ru.socialnet.team29.dto.PersonSearchDto;
import ru.socialnet.team29.dto.RecommendationFriendsDto;
import ru.socialnet.team29.model.FriendForFront;

import java.util.List;
import java.util.Map;

public interface FriendService {
    /**
     * Запрос на добавление друга
     * @param friendId идентификатор друга
     * @return OK - в случае успеха
     */
    HttpStatus addFriendRequest(Integer friendId);

    /**
     * Подтверждение дружбы
     * @param friendId идентификатор друга
     * @return OK - в случае успеха
     */
    HttpStatus approveFriendship(Integer friendId);

    /**
     * Получить всех друзей персоны
     * @param params набор параметров
     * @return список друзей класса FriendForFront
     */
    AnswerListFriendsForPerson<FriendForFront> getAllFriendsForPerson(Map<String, String> params);

    /**
     * Удалдение друга
     * @param friendId идентификатор друга
     * @return OK - в случае успеха
     */
    HttpStatus deleteFriendship(Integer friendId);

    /**
     * Проверка друга
     * @param friendId идентификатор друга
     * @return OK - в случае успеха
     */
    HttpStatus friendsByIdExists(Integer friendId);

    /**
     * Получить количество запросов на дружбу
     * @return количество запросов на дружбу
     */
    Integer getCountOfFriends();

    /**
     * Получение всех Id друзей
     * @return список id всех друзей
     */
    FriendSearchDto getAllFriendIds();

    /**
     * Подписка на участника соц. сети
     * @param friendId идентификатор наблюдаемого
     * @return OK - в случае успеха
     */
    HttpStatus toSubscribe(Integer friendId);

    /**
     * Рекомендации
     * @return List<RecommendationFriendsDto> список рекомендаций друзей
     */
    List<RecommendationFriendsDto> getRecommendations();

    /**
     * Блокировка друга
     * @param friendId идентификатор друга
     * @return OK - в случае успеха
     */
    HttpStatus blockFriend(Integer friendId);

    /**
     * Получение id заблокированных друзей
     * @return информация о заблокированных друзьях
     */
    FriendSearchDto getIdsBlockedFriends();

    List<Integer> getAllFriendsId(Integer idCurrentUser);
}
