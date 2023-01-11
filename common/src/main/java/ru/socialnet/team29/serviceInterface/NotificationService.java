package ru.socialnet.team29.serviceInterface;

import ru.socialnet.team29.answers.AddNewNotification;
import ru.socialnet.team29.answers.DataNotificationSettings;
import ru.socialnet.team29.answers.NotificationForFront;
import ru.socialnet.team29.payloads.AddNotificationPayload;
import ru.socialnet.team29.payloads.ChangeNotificationSettingsPayload;

import java.util.List;


public interface NotificationService
{
    Integer getCountAllNotifications(int idPerson);

    List<NotificationForFront> getAllNotificationsForPerson(int id);

    AddNewNotification addNewNotification(AddNotificationPayload payload);

    Integer changeNotificationSettingsStatus(ChangeNotificationSettingsPayload payload);

    DataNotificationSettings getSettings();
}
