package ru.socialnet.team29.repository;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;
import ru.socialnet.team29.domain.tables.PersonToNotificationSettings;
import ru.socialnet.team29.domain.tables.records.PersonToNotificationSettingsRecord;

import java.util.List;

@Repository
public class NotificationSettingsRepository implements CrudRepository<PersonToNotificationSettingsRecord>{
    private DSLContext dsl;
    @Autowired
    public void setDsl(@Lazy DSLContext dsl) {
        this.dsl = dsl;
    }

    public int insert(PersonToNotificationSettingsRecord settingsRecord) {
        return dsl.insertInto(PersonToNotificationSettings.PERSON_TO_NOTIFICATION_SETTINGS)
                .set(dsl.newRecord(PersonToNotificationSettings.PERSON_TO_NOTIFICATION_SETTINGS, settingsRecord))
                .onDuplicateKeyUpdate()
                .set(dsl.newRecord(PersonToNotificationSettings.PERSON_TO_NOTIFICATION_SETTINGS, settingsRecord))
                .execute();
    }

    @Override
    public PersonToNotificationSettingsRecord findById(int id) {
        return null;
    }

    @Override
    public PersonToNotificationSettingsRecord update(PersonToNotificationSettingsRecord settingsRecord) {
        return null;
    }

    @Override
    public boolean delete(int id) {
        return false;
    }

    public Integer delete(int userId, int notificationTypeId) {
        return dsl.deleteFrom(PersonToNotificationSettings.PERSON_TO_NOTIFICATION_SETTINGS)
                .where(PersonToNotificationSettings.PERSON_TO_NOTIFICATION_SETTINGS.NOTIFICATION_TYPE_ID.eq(notificationTypeId))
                .and(PersonToNotificationSettings.PERSON_TO_NOTIFICATION_SETTINGS.USER_ID.eq(userId))
                .execute();
    }


    public List<PersonToNotificationSettingsRecord> getNotificationSettingsForCurrentUser(Integer userId) {
        return dsl.selectFrom(PersonToNotificationSettings.PERSON_TO_NOTIFICATION_SETTINGS)
                .where(PersonToNotificationSettings.PERSON_TO_NOTIFICATION_SETTINGS.USER_ID.eq(userId))
                .fetch();
    }

}
