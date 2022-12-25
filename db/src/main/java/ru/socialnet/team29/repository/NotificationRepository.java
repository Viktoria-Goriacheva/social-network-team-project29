package ru.socialnet.team29.repository;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;
import ru.socialnet.team29.domain.tables.Notification;
import ru.socialnet.team29.model.NotificationCommon;
import ru.socialnet.team29.services.DslContextCustom;

import java.util.List;

@Repository
public class NotificationRepository
{

    private static DSLContext dsl;

    @Autowired
    public void setDsl(@Lazy DSLContext dsl) {
        this.dsl = dsl;
    }


    public Integer getCountNotificationByPersonId(Integer id) {
        return dsl.selectFrom(Notification.NOTIFICATION)
                .where(Notification.NOTIFICATION.PERSON_ID.eq(id))
                .fetch()
                .size();
    }

    public List<NotificationCommon> getAllNotificationsByPersonsId(Integer idPerson) {
        return dsl.selectFrom(Notification.NOTIFICATION)
                .where(Notification.NOTIFICATION.PERSON_ID.eq(idPerson))
                .fetch()
                .into(NotificationCommon.class);

    }

    public NotificationCommon addNewNotification(NotificationCommon notificationCommon) throws NullPointerException {
        return dsl.insertInto(Notification.NOTIFICATION)
                .set(dsl.newRecord(Notification.NOTIFICATION, notificationCommon))
                .returning()
                .fetchOne()
                .into(NotificationCommon.class);
    }


}