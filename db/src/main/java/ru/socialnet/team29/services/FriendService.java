package ru.socialnet.team29.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.socialnet.team29.answers.AnswerListFriendsForPerson;
import ru.socialnet.team29.domain.tables.Person;
import ru.socialnet.team29.domain.tables.records.FriendshipRecord;
import ru.socialnet.team29.domain.tables.records.PersonRecord;
import ru.socialnet.team29.mappers.FriendMapperImpl;
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
        return newFriendshipId > 0;
    }

    public Boolean approveFriendship(Integer id, Integer friendId) {
        boolean isExistsFriend = !personService.findAll(Person.PERSON.ID.eq(friendId)).isEmpty();
        if (!isExistsFriend) {
            return false;
        }
        boolean isExistsFriendship = false;
        FriendshipStatus statusRequest = FriendshipStatus.valueOf("REQUEST_TO");
        try {
            isExistsFriendship = friendRepository.getFriendshipStatusByIdAndFriendId(friendId, id).equals(statusRequest);
        } catch (NullPointerException ex) {
            log.info("Не найден друг с id(" + friendId + ") и статусом 'REQUEST_TO'");
            return false;
        }
        Long newFriendshipId = 0L;
        if (isExistsFriendship) {
            newFriendshipId = friendRepository.updateFriendship(id, friendId, "FRIEND");
        }
        return newFriendshipId > 0;
    }

    public AnswerListFriendsForPerson getFriendsByIdPerson(
            Integer id,
            String statusName,
            AnswerListFriendsForPerson.FriendPageable pageable) {
        List<FriendshipRecord> friendRecords =  friendRepository.getFriendsByIdPerson(id, statusName, pageable);
        int totalFriendshipRecords = friendRepository.getCountOfFriends(id, statusName);
        if (friendRecords == null) {
            return null;
        }
        List<PersonRecord> personRecords = new ArrayList<>();
        if (!statusName.equals("")) {
            List<Integer> friendIds = friendRecords.stream()
                    .map(rec -> Integer.valueOf(rec.getDstPersonId())).toList();
            personRecords = personService.findAll(Person.PERSON.ID.in(friendIds));
        }
        var result = friendMapper.PersonRecordToFriendForFront(personRecords)
                .stream().peek(friend -> friend.setStatusCode(statusName)).collect(Collectors.toList());
        int lastPageNumber = (int) Math.ceil(totalFriendshipRecords / pageable.getPageSize());
        return AnswerListFriendsForPerson.builder()
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
        return friendRepository.deleteFriendship(id, friendId);
    }

    public Boolean friendsByIdExists(Integer id, Integer friendId) {
        return friendRepository.friendsByIdExists(id, friendId);
    }

    public Integer getCountOfFriends(Integer id) {
        return friendRepository.getCountOfFriends(id, "FRIEND");
    }
}
