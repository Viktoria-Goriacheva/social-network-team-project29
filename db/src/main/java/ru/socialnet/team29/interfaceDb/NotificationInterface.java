package ru.socialnet.team29.interfaceDb;

import ru.socialnet.team29.answers.AddNewNotification;
import ru.socialnet.team29.answers.NotificationForFront;
import ru.socialnet.team29.payloads.AddNotificationPayload;

import java.util.List;

public interface NotificationInterface {
    Integer getCountNotificationsById(Integer id);

    List<NotificationForFront> getAllNotificationsById(Integer idPerson);

    AddNewNotification saveNewNotification(AddNotificationPayload payload);
}
