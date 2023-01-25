package ru.socialnet.team29.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;
import ru.socialnet.team29.answers.AnswerListFriendsForPerson;
import ru.socialnet.team29.domain.tables.Friendship;
import ru.socialnet.team29.domain.tables.records.FriendshipRecord;
import ru.socialnet.team29.dto.PersonSearchDto;
import ru.socialnet.team29.model.enums.FriendshipStatus;

import java.util.List;
import java.util.Objects;

import static org.jooq.impl.DSL.val;

@Repository
@Slf4j
@RequiredArgsConstructor
public class FriendRepository {

    private DSLContext dsl;

    @Autowired
    public void setDsl(@Lazy DSLContext dsl) {
        this.dsl = dsl;
    }

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
     * @param params параметры отбора
     * @return список персон согласно статусу дружбы и параметрам отбора
     */
    public List<FriendshipRecord> getFriendsByIdPerson(AnswerListFriendsForPerson<PersonSearchDto> params) {
        FriendshipStatus friendshipStatus = FriendshipStatus.valueOf(params.getContent().get(0).getStatusCode());
        Friendship friendship = Friendship.FRIENDSHIP;
        return dsl.selectFrom(friendship)
                .where(
                        friendship.STATUS_ID.eq(friendshipStatus.getNumber().toString())
                                .or(val(friendshipStatus.getValue()).eq("NONE")),
                        friendship.SRC_PERSON_ID.eq(params.getId().toString())
                )
                .orderBy(friendship.DST_PERSON_ID)
                .limit(params.getPageable().getPageSize())
                .offset(params.getPageable().getOffset())
                .fetchInto(FriendshipRecord.class);
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
        FriendshipStatus friendshipStatus = FriendshipStatus.valueOf(statusName);
        Friendship friendship = Friendship.FRIENDSHIP;
        return dsl.select()
            .from(friendship)
            .where(
                    friendship.SRC_PERSON_ID.eq(id.toString()),
                    friendship.STATUS_ID.eq(String.valueOf(friendshipStatus.getNumber())))
            .fetch()
            .size();
    }

    /**
     * Получить все идентификаторы друзей
     * @param id идентификатор персоны
     * @return список идентификаторов друзей персоны
     */
    public List<Integer> getAllFriendIds(Integer id, FriendshipStatus friendshipStatus) {
        Friendship friendship = Friendship.FRIENDSHIP;
        return dsl.selectFrom(friendship)
                .where(
                        friendship.SRC_PERSON_ID.eq(id.toString())
                        .and(friendship.STATUS_ID.eq(String.valueOf(friendshipStatus.getNumber()))))
                .fetch(frship -> Integer.parseInt(frship.getDstPersonId()));
    }


    /**
     * Получить список идентификаторов рекомендованных друзей. Отбираются идентификаторы друзей друзей
     * @param id идентификатор персоны
     * @return список идентификаторов рекомендованных друзей
     */
    public List<Integer> getRecommendations(Integer id) {
        FriendshipStatus friendStatus = FriendshipStatus.FRIEND;
        String sqlQuery = "select fr2.dst_person_id from socialnet.friendship as fr2 " +
                "right join (" +
                "select * from socialnet.friendship as fr " +
                "where fr.src_person_id = {0} and fr.status_id = {1}" +
                ") as fr3 " +
                "on fr2.src_person_id = fr3.dst_person_id " +
                "where fr2.status_id = {1} " +
                "and fr2.dst_person_id <> {0} " +
                "and not fr2.dst_person_id in(select fr.dst_person_id from socialnet.friendship as fr " +
                "where fr.src_person_id = {0})";
        return (List<Integer>) dsl.resultQuery(sqlQuery, id.toString(), friendStatus.getNumber().toString()).fetch(0);
    }

    public FriendshipRecord getFriendshipStatus(Integer id, Integer friendId) {
        Friendship friendship = Friendship.FRIENDSHIP;
        return dsl.selectFrom(friendship)
                .where(
                        friendship.SRC_PERSON_ID.eq(id.toString()),
                        friendship.DST_PERSON_ID.eq(friendId.toString()))
                .fetchOne();
    }
}
