package ru.socialnet.team29.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.socialnet.team29.answers.AnswerListFriendsForPerson;
import ru.socialnet.team29.domain.tables.Person;
import ru.socialnet.team29.domain.tables.records.FriendshipRecord;
import ru.socialnet.team29.domain.tables.records.PersonRecord;
import ru.socialnet.team29.dto.FriendSearchDto;
import ru.socialnet.team29.dto.RecommendationFriendsDto;
import ru.socialnet.team29.mappers.FriendMapperImpl;
import ru.socialnet.team29.model.FriendForFront;
import ru.socialnet.team29.model.enums.FriendshipStatus;
import ru.socialnet.team29.repository.FriendRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Сервис по предоставлению всей информации по дружеским отношениям
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class FriendService {
    private final FriendRepository friendRepository;
    private final PersonService personService;
    private final FriendMapperImpl friendMapper;

    public Boolean addFriendRequest(Integer id, Integer friendId) {
        boolean isExistsFriend = !personService.findAll(Person.PERSON.ID.eq(friendId)).isEmpty();
        if (!isExistsFriend) {
            return false;
        }
        boolean isExistsFriendship = friendRepository.friendsByIdExists(id, friendId);
        long newFriendshipId;
        if (isExistsFriendship) {
            newFriendshipId = friendRepository.updateFriendship(id, friendId, "REQUEST_TO");
        } else {
            newFriendshipId = friendRepository.insertFriendship(id, friendId, "REQUEST_TO");
        }

        isExistsFriendship = friendRepository.friendsByIdExists(friendId, id);
        if (isExistsFriendship) {
            newFriendshipId += friendRepository.updateFriendship(friendId, id, "REQUEST_FROM");
        } else {
            newFriendshipId += friendRepository.insertFriendship(friendId, id, "REQUEST_FROM");
        }
        return newFriendshipId > 0;
    }

    public Boolean approveFriendship(Integer id, Integer friendId) {
        boolean isExistsFriend = !personService.findAll(Person.PERSON.ID.eq(friendId)).isEmpty();
        if (!isExistsFriend) {
            return false;
        }
        boolean isExistsFriendship;
        FriendshipStatus statusRequest = FriendshipStatus.valueOf("REQUEST_FROM");
        try {
            isExistsFriendship = friendRepository.getFriendshipStatusByIdAndFriendId(id, friendId).equals(statusRequest);
        } catch (NullPointerException ex) {
            log.info("Не найден друг с id(" + friendId + ") и статусом 'REQUEST_FROM'");
            return false;
        }
        Long newFriendshipId = 0L;
        if (isExistsFriendship) {
            // Делаем зеркальные дружеские отношения
            newFriendshipId = friendRepository.updateFriendship(friendId, id, "FRIEND");
            friendRepository.updateFriendship(id, friendId, "FRIEND");
        }
        return newFriendshipId > 0;
    }

    public AnswerListFriendsForPerson<FriendForFront> getFriendsByIdPerson(
            Integer id,
            String statusName,
            AnswerListFriendsForPerson.FriendPageable pageable) {
        List<FriendshipRecord> friendRecords =  friendRepository.getFriendsByIdPerson(id, statusName, pageable);
        int totalFriendshipRecords = friendRepository.getCountOfFriends(id, statusName);
        if (friendRecords == null) {
            return null;
        }
        List<PersonRecord> personRecords = new ArrayList<>();
        List<Integer> friendIds;
        if (!statusName.equals("")) {
            friendIds = friendRecords.stream()
                    .map(rec -> Integer.valueOf(rec.getDstPersonId())).toList();
            personRecords = personService.findAll(Person.PERSON.ID.in(friendIds));
        }
        var result = friendMapper.PersonRecordToFriendForFront(personRecords)
                .stream().peek(friend -> friend.setStatusCode(statusName)).collect(Collectors.toList());
        int lastPageNumber = (int) Math.ceil(totalFriendshipRecords / pageable.getPageSize());
        return AnswerListFriendsForPerson.<FriendForFront>builder()
                .totalElements(totalFriendshipRecords)
                .totalPages(lastPageNumber)
                .content(result)
                .pageable(pageable)
                .first(pageable.getPageNumber() == 1)
                .last(pageable.getPageNumber() == lastPageNumber)
                .empty(totalFriendshipRecords == 0)
                .size(result.size())
                .build();
    }

    public Boolean deleteFriend(Integer id, Integer friendId) {
        Boolean result = friendRepository.deleteFriendship(id, friendId);
        if (friendsByIdExists(friendId, id)) {
            result = result && friendRepository.deleteFriendship(friendId, id);
        }
        return result;
    }

    public Boolean friendsByIdExists(Integer id, Integer friendId) {
        return friendRepository.friendsByIdExists(id, friendId);
    }

    public Integer getCountOfFriends(Integer id) {
        return friendRepository.getCountOfFriends(id, "FRIEND");
    }

    public Integer getCountOfRequestFrom(Integer id) {
        return friendRepository.getCountOfFriends(id, "REQUEST_FROM");
    }

    public FriendSearchDto getAllFriendIds(Integer id) {
        List<Integer> ids = friendRepository.getAllFriendIds(id, FriendshipStatus.FRIEND);
        return FriendSearchDto.builder()
                .ids(ids)
                .statusCode("FRIEND")
                .build();
    }

    public Boolean toSubscribe(Integer id, Integer friendId) {
        boolean isExistsFriend = !personService.findAll(Person.PERSON.ID.eq(friendId)).isEmpty();
        if (!isExistsFriend) {
            return false;
        }
        boolean isExistsFriendship = friendRepository.friendsByIdExists(id, friendId);
        // Если никаких дружеских отношений не было, тогда подписываемся
        if (!isExistsFriendship) {
            return friendRepository.insertFriendship(id, friendId, "SUBSCRIBED") > 0;
        }
        return false;
    }

    public List<RecommendationFriendsDto> getRecommendations(Integer id) {
        List<Integer> recommendationFriendsIds = friendRepository.getRecommendations(id);
        List<PersonRecord> personRecords = personService.findAll(Person.PERSON.ID.in(recommendationFriendsIds));
        return friendMapper.PersonRecordsToRecommendationFriendsDto(personRecords);
    }

    public Boolean blockFriend(Integer id, Integer friendId) {
        Boolean result = false;
        if (friendsByIdExists(id, friendId)) {
            FriendshipStatus statusBlocked = FriendshipStatus.valueOf("BLOCKED");
            if (friendRepository.getFriendshipStatusByIdAndFriendId(id, friendId).equals(statusBlocked)) {
                result = friendRepository.updateFriendship(id, friendId, "FRIEND") > 0;
            } else {
                result = friendRepository.updateFriendship(id, friendId, "BLOCKED") > 0;
            }
        } else {
            result = friendRepository.insertFriendship(id, friendId, "BLOCKED") > 0;
        }
        return result;
    }

    public FriendSearchDto getIdsBlockedFriends(Integer id) {
        List<Integer> blockedFriendIds = friendRepository.getAllFriendIds(id, FriendshipStatus.BLOCKED);
        return FriendSearchDto.builder()
                .ids(blockedFriendIds)
                .statusCode("BLOCKED")
                .build();
    }
}
