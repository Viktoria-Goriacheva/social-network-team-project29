package ru.socialnet.team29.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.socialnet.team29.answers.AnswerListFriendsForPerson;
import ru.socialnet.team29.dto.FriendSearchDto;
import ru.socialnet.team29.dto.PersonSearchDto;
import ru.socialnet.team29.dto.RecommendationFriendsDto;
import ru.socialnet.team29.model.FriendForFront;
import ru.socialnet.team29.model.enums.NotificationType;
import ru.socialnet.team29.payloads.AddNotificationPayload;
import ru.socialnet.team29.serviceInterface.FriendService;
import ru.socialnet.team29.serviceInterface.feign.DBConnectionFeignInterface;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class FriendServiceImpl implements FriendService {
    private final DBConnectionFeignInterface feignInterfaceFriend;
    private final PersonServiceImpl personService;
    private final NotificationServiceImpl notificationService;

    @Override
    public HttpStatus addFriendRequest(Integer friendId) {
        if (friendId != null) {
            log.info("Add friend id=" + friendId.toString());
            int id = personService.getIdPersonFromSecurityContext();
            boolean isAdded = feignInterfaceFriend.addFriendRequest(id, friendId);
            if (!isAdded) {
                log.info("The friend is not added");
                return HttpStatus.BAD_REQUEST;
            } else {
                AddNotificationPayload payload = AddNotificationPayload.builder()
                        .authorId(String.valueOf(id))
                        .userId(String.valueOf(friendId))
                        .notificationType(NotificationType.FRIEND_REQUEST.getValue())
                        .content("Вас добавили в друзья. Проверьте кто бы это мог быть?")
                        .NotificationTypeId(NotificationType.FRIEND_REQUEST.getNumber())
                        .build();
                notificationService.addNewNotification(payload);
            }
        } else {
            log.info("Add friend id=null");
            return HttpStatus.BAD_REQUEST;
        }
        return HttpStatus.OK;
    }

    @Override
    public HttpStatus approveFriendship(Integer friendId) {
        if (friendId != null) {
            log.info("Approve friend id=" + friendId.toString());
            int id = personService.getIdPersonFromSecurityContext();
            boolean isApproved = feignInterfaceFriend.approveFriendship(id, friendId);
            if (!isApproved) {
                log.info("The friend is not Approved");
                return HttpStatus.BAD_REQUEST;
            }
        } else {
            log.info("The friend id=null");
            return HttpStatus.BAD_REQUEST;
        }
        return HttpStatus.OK;
    }

    @Override
    public AnswerListFriendsForPerson<FriendForFront> getAllFriendsForPerson(Map<String, String> params) {
        log.info("Получаем запрос от фронта на получение всех друзей для  " + SecurityContextHolder.getContext().getAuthentication().getName() + " с параметрами " + params.toString());
        PersonSearchDto personSearchDto = PersonSearchDto.builder()
                .ageTo(Integer.parseInt(params.getOrDefault("ageTo", "0")))
                .ageFrom(Integer.parseInt(params.getOrDefault("ageFrom", "0")))
                .birthDateFrom(params.getOrDefault("birthDateFrom", "1900-01-01T00:00:00+00:00"))
                .birthDateTo(params.getOrDefault("birthDateTo", "3000-01-01T00:00:00+00:00"))
                .city(params.getOrDefault("city", null))
                .country(params.getOrDefault("country", null))
                .firstName(params.getOrDefault("firstName", null))
                .statusCode(params.getOrDefault("statusCode", "NONE"))
                .page(Integer.parseInt(params.getOrDefault("page", "1")))
//                .size(Integer.parseInt(params.getOrDefault("size", "20")))
                .build();

        if (personSearchDto.getAgeFrom() > 0) {
            personSearchDto.setBirthDateTo(
                    OffsetDateTime.now().minusYears(personSearchDto.getAgeFrom()).toString()
            );
        }

        if (personSearchDto.getAgeTo() > 0) {
            personSearchDto.setBirthDateFrom(
                    OffsetDateTime.now().minusYears(personSearchDto.getAgeTo()).toString()
            );
        }

        int id = personService.getIdPersonFromSecurityContext();
        AnswerListFriendsForPerson.FriendPageable pageable = AnswerListFriendsForPerson.FriendPageable.builder()
                .pageNumber(personSearchDto.getPage())
                .pageSize(personSearchDto.getSize())
                .build();
        AnswerListFriendsForPerson<PersonSearchDto> answerListFriendsForPerson = AnswerListFriendsForPerson.<PersonSearchDto>builder()
                .pageable(pageable)
                .content(List.of(personSearchDto))
                .id(id)
                .build();
        return feignInterfaceFriend.getFriendsByIdPerson(answerListFriendsForPerson);
    }

    @Override
    public HttpStatus deleteFriendship(Integer friendId) {
        if (friendId != null) {
            log.info("Delete friend id=" + friendId.toString());
            int id = personService.getIdPersonFromSecurityContext();
            boolean isDeleted = feignInterfaceFriend.deleteFriendship(id, friendId);
            if (!isDeleted) {
                log.info("The friend is not deleted");
                return HttpStatus.BAD_REQUEST;
            }
        } else {
            log.info("The deleted friend id=null");
            return HttpStatus.BAD_REQUEST;
        }
        return HttpStatus.OK;
    }

    @Override
    public HttpStatus friendsByIdExists(Integer friendId) {
        if (friendId != null) {
            log.info("Get friend by id=" + friendId.toString());
            int id = personService.getIdPersonFromSecurityContext();
            boolean isExists = feignInterfaceFriend.friendsByIdExists(id, friendId);
            if (!isExists) {
                log.info("The friend is not exists");
                return HttpStatus.BAD_REQUEST;
            }
        } else {
            log.info("The friend id=null");
            return HttpStatus.BAD_REQUEST;
        }
        return HttpStatus.OK;
    }

    @Override
    public Integer getCountOfFriends() {
        log.info("Получаем запрос от фронта на получение количества запросов на дружбу для  " + SecurityContextHolder.getContext().getAuthentication().getName());
        int id = personService.getIdPersonFromSecurityContext();
        return feignInterfaceFriend.getCountOfFriends(id);
    }

    @Override
    public FriendSearchDto getAllFriendIds() {
        log.info("Получаем запрос от фронта на выдачу всех id друзей для " + SecurityContextHolder.getContext().getAuthentication().getName());
        int id = personService.getIdPersonFromSecurityContext();
        return feignInterfaceFriend.getAllFriendIds(id);
    }

    @Override
    public HttpStatus toSubscribe(Integer friendId) {
        log.info("Получаем запрос от фронта на подписку персоны "
                + SecurityContextHolder.getContext().getAuthentication().getName()
                + " на наблюдаемого");
        if (friendId != null) {
            log.info("Get friend by id=" + friendId.toString());
            int id = personService.getIdPersonFromSecurityContext();
            boolean isSubscribed = feignInterfaceFriend.toSubscribe(id, friendId);
            if (!isSubscribed) {
                log.info("The friend is not subscribed");
                return HttpStatus.BAD_REQUEST;
            }
        } else {
            log.info("The subscribed friend id=null");
            return HttpStatus.BAD_REQUEST;
        }
        return HttpStatus.OK;
    }

    @Override
    public List<RecommendationFriendsDto> getRecommendations() {
        log.info("Получаем запрос от фронта на выдачу всех id рекомендованных друзей для " + SecurityContextHolder.getContext().getAuthentication().getName());
        int id = personService.getIdPersonFromSecurityContext();
        return feignInterfaceFriend.getRecommendations(id);
    }

    @Override
    public HttpStatus blockFriend(Integer friendId) {
        log.info("Получаем запрос от фронта на блокировку друга "
                + SecurityContextHolder.getContext().getAuthentication().getName());
        if (friendId != null) {
            log.info("Блокируемый друг id=" + friendId);
            int id = personService.getIdPersonFromSecurityContext();
            boolean isBlocked = feignInterfaceFriend.blockFriend(id, friendId);
            if (!isBlocked) {
                log.info("Не удалось заблокировать друга id=" + friendId);
                return HttpStatus.BAD_REQUEST;
            }
        } else {
            log.info("The subscribed friend id=null");
            return HttpStatus.BAD_REQUEST;
        }
        return HttpStatus.OK;
    }

    @Override
    public FriendSearchDto getIdsBlockedFriends() {
        log.info("Получаем запрос от фронта на выдачу всех id заблокированных друзей для " + SecurityContextHolder.getContext().getAuthentication().getName());
        int id = personService.getIdPersonFromSecurityContext();
        return feignInterfaceFriend.getIdsBlockedFriends(id);
    }

    @Override
    public List<Integer> getAllFriendsId(Integer idCurrentUser) {
        return feignInterfaceFriend.getListIdsAllFriendsCurrentUser(idCurrentUser);
    }
}
