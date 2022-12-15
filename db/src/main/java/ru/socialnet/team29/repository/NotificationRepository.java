package ru.socialnet.team29.repository;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import ru.socialnet.team29.domain.tables.Notification;
import ru.socialnet.team29.model.NotificationCommon;
import ru.socialnet.team29.services.DslContextCustom;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class NotificationRepository
{

    private final DslContextCustom dslContextCustom;
    private static DSLContext dsl;


    private void initDsl() {
        if (dsl == null) {
            dsl = dslContextCustom.initDslContext();
        }
    }

    public Integer getCountNotificationByPersonId(Integer id) {
        initDsl();
        return dsl.selectFrom(Notification.NOTIFICATION)
                .where(Notification.NOTIFICATION.PERSON_ID.eq(id))
                .fetch()
                .size();
    }

    public List<NotificationCommon> getAllNotificationsByPersonsId(Integer idPerson) {
        initDsl();
        return dsl.selectFrom(Notification.NOTIFICATION)
                .where(Notification.NOTIFICATION.PERSON_ID.eq(idPerson))
                .fetch()
                .into(NotificationCommon.class);

    }

    public NotificationCommon addNewNotification(NotificationCommon notificationCommon) throws NullPointerException {
        initDsl();
        return dsl.insertInto(Notification.NOTIFICATION)
                .set(dsl.newRecord(Notification.NOTIFICATION, notificationCommon))
                .returning()
                .fetchOne()
                .into(NotificationCommon.class);
    }


}