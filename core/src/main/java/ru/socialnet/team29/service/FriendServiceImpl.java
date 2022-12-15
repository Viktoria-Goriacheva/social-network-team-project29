package ru.socialnet.team29.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.socialnet.team29.answers.AnswerListFriendsForPerson;
import ru.socialnet.team29.model.FriendForFront;
import ru.socialnet.team29.serviceInterface.FriendService;
import ru.socialnet.team29.serviceInterface.feign.DBConnectionFeignInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class FriendServiceImpl implements FriendService {
    private final DBConnectionFeignInterface feignInterfaceFriend;
    private final PersonServiceImpl personService;

    @Override
    public HttpStatus addFriendRequest(Integer friendId) {
        if (friendId != null) {
            log.info("Add friend id=" + friendId.toString());
            int id = personService.getIdPersonFromSecurityContext();
            boolean isAdded = feignInterfaceFriend.addFriendRequest(id, friendId);
            if (!isAdded) {
                log.info("The friend is not added");
                return HttpStatus.BAD_REQUEST;
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
    public AnswerListFriendsForPerson getAllFriendsForPerson(Map<String, String> params) {
        log.info("Получаем запрос от фронта на получение всех друзей для  " + SecurityContextHolder.getContext().getAuthentication().getName() + " с параметрами " + params.toString());
        String statusName = params.getOrDefault("statusCode", "");
        int pageNumber = Integer.parseInt(params.getOrDefault("page", "1"));
        int sizePage = Integer.parseInt(params.getOrDefault("size", "20"));
        int id = personService.getIdPersonFromSecurityContext();
        sizePage = Math.max(1, sizePage);
        AnswerListFriendsForPerson.FriendPageable pageable = AnswerListFriendsForPerson.FriendPageable.builder()
                .pageNumber(pageNumber)
                .pageSize(sizePage)
                .build();
        AnswerListFriendsForPerson answerListFriends = feignInterfaceFriend.getFriendsByIdPerson(id, statusName, pageable);
        List<FriendForFront> listFriends = answerListFriends.getContent();
        int totalPages = (int)Math.ceil(listFriends.size() / sizePage);
        return AnswerListFriendsForPerson.builder()
                    .content(listFriends)
                    .empty(listFriends.size() == 0)
                    .numberOfElements(listFriends.size())
                    .totalElements(listFriends.size())
                    .sort(new ArrayList<>())
                    .totalPages(totalPages)
                    .pageable(pageable)
                    .build();
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
    public Integer getCountOfFriends() {
        log.info("Получаем запрос от фронта на получение количества всех друзей для  " + SecurityContextHolder.getContext().getAuthentication().getName());
        int id = personService.getIdPersonFromSecurityContext();
        return feignInterfaceFriend.getCountOfFriends(id);
    }
}
