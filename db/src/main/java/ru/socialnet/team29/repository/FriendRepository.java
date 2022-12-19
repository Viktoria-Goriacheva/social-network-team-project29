package ru.socialnet.team29.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import ru.socialnet.team29.answers.AnswerListFriendsForPerson;
import ru.socialnet.team29.domain.tables.Friendship;
import ru.socialnet.team29.domain.tables.records.FriendshipRecord;
import ru.socialnet.team29.model.enums.FriendshipStatus;

import java.util.List;
import java.util.Objects;

@Repository
@Slf4j
@RequiredArgsConstructor
public class FriendRepository {

    private final DSLContext dsl;

    /**
     * Обновить статус дружеского отношения
     * @param id идентификатор персоны
     * @param friendId идентификатор друга
     * @param statusName представление статуса
     * @return идентификатор измененного отношения
     */
    public Long updateFriendship(Integer id, Integer friendId, String statusName) {
        var status = FriendshipStatus.valueOf(statusName);
        Friendship friendship = Friendship.FRIENDSHIP;
        return dsl.update(friendship)
                .set(friendship.STATUS_ID, status.getNumber().toString())
                .where(friendship.SRC_PERSON_ID.eq(id.toString()), friendship.DST_PERSON_ID.eq(friendId.toString()))
                .returningResult(friendship.ID)
                .fetchOne()
                .getValue(friendship.ID);
    }

    /**
     * Добавить запись о новом дружеском отношении
     * @param id идентификатор персоны
     * @param friendId идентификатор друга
     * @param statusName представление статуса
     * @return идентификатор нового отношения
     */
    public Long insertFriendship(Integer id, Integer friendId, String statusName) {
        var status = FriendshipStatus.valueOf(statusName);
        Friendship friendship = Friendship.FRIENDSHIP;
        return dsl.insertInto(friendship)
                .set(friendship.STATUS_ID, status.getNumber().toString())
                .set(friendship.SRC_PERSON_ID, id.toString())
                .set(friendship.DST_PERSON_ID, friendId.toString())
                .returningResult(friendship.ID)
                .fetchOne()
                .getValue(friendship.ID);
    }

    /**
     * Получить статус дружбы по идентификаторам персоны и друга
     * @param id идентификатор персоны
     * @param friendId идентификатор друга
     * @return статус
     */
     public FriendshipStatus getFriendshipStatusByIdAndFriendId(Integer id, Integer friendId) {
        Friendship friendship = Friendship.FRIENDSHIP;
        String statusId =  Objects.requireNonNull(dsl.selectFrom(friendship)
                .where(
                    friendship.SRC_PERSON_ID.eq(id.toString())
                        .and(friendship.DST_PERSON_ID.eq(friendId.toString())))
                .fetchOne())
            .getStatusId();
        return FriendshipStatus.getEnum(statusId);
    }

    /**
     * Получение списка записей о друзьях из БД
     * @param id идентификатор персоны
     * @param statusName представление статуса дружбы
     * @param pageable объект настройки пагинации FriendPageable
     * @return список персон согласно статусу дружбы
     */
    public List<FriendshipRecord> getFriendsByIdPerson(
            Integer id,
            String statusName,
            AnswerListFriendsForPerson.FriendPageable pageable) {
        try {
            var statusFRIEND = FriendshipStatus.valueOf(statusName);
            Friendship friendship = Friendship.FRIENDSHIP;
            return dsl.selectFrom(friendship)
                    .where(
                            friendship.STATUS_ID.eq(statusFRIEND.getNumber().toString())
                                    .and(friendship.SRC_PERSON_ID.eq(id.toString())))
                    .orderBy(friendship.DST_PERSON_ID)
                    .limit(pageable.getPageSize())
                    .offset(pageable.getOffset())
                    .fetchInto(FriendshipRecord.class);
        } catch (NullPointerException exp) {
            return null;
        }

    }

    /**
     * Удалдение друга
     * @param id идентификатор персоны
     * @param friendId идентификатор друга
     * @return true - в случае успеха
     */
    public Boolean deleteFriendship(Integer id, Integer friendId) {
        Friendship friendship = Friendship.FRIENDSHIP;
        return dsl.deleteFrom(friendship)
                .where(
                    friendship.SRC_PERSON_ID.eq(id.toString())
                    .and(friendship.DST_PERSON_ID.eq(friendId.toString())))
                .execute() == 1;
    }

    /**
     * Проверка друга
     * @param id идентификатор персоны
     * @param friendId идентификатор друга
     * @return true - в случае успеха
     */
    public Boolean friendsByIdExists(Integer id, Integer friendId) {
        Friendship friendship = Friendship.FRIENDSHIP;
        return dsl.selectFrom(friendship)
                .where(
                    friendship.SRC_PERSON_ID.eq(id.toString())
                    .and(friendship.DST_PERSON_ID.eq(friendId.toString())))
                .fetch()
                .size() == 1;
    }

    /**
     * Получить количество друзей
     * @param id идентификатор персоны
     * @return количество друзей персоны
     */
    public Integer getCountOfFriends(Integer id, String statusName) {
        var statusFRIEND = FriendshipStatus.valueOf(statusName);
        Friendship friendship = Friendship.FRIENDSHIP;
        return dsl.select()
            .from(friendship)
            .where(
                friendship.SRC_PERSON_ID.eq(id.toString())
                    .and(friendship.STATUS_ID.eq(String.valueOf(statusFRIEND.getNumber()))))
            .fetch()
            .size();
    }
}
