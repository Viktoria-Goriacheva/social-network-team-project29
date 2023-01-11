package ru.socialnet.team29.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.socialnet.team29.answers.AddNewNotification;
import ru.socialnet.team29.answers.DataNotificationSettings;
import ru.socialnet.team29.answers.NotificationForFront;
import ru.socialnet.team29.answers.NotificationUnitSettings;
import ru.socialnet.team29.model.enums.NotificationType;
import ru.socialnet.team29.payloads.AddNotificationPayload;
import ru.socialnet.team29.payloads.ChangeNotificationSettingsPayload;
import ru.socialnet.team29.serviceInterface.NotificationService;
import ru.socialnet.team29.serviceInterface.feign.DBConnectionFeignInterfaceNotification;


import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService
{
private final DBConnectionFeignInterfaceNotification feignInterfaceNotification;
private final PersonServiceImpl personService;

    @Override
    public Integer getCountAllNotifications(int idPerson) {
        return feignInterfaceNotification.getCountNotification(idPerson);
    }

    @Override
    public List<NotificationForFront> getAllNotificationsForPerson(int id) {
        return feignInterfaceNotification.getAllNotificationsById(id);
    }

    @Override
    public AddNewNotification addNewNotification(AddNotificationPayload payload) {
        return feignInterfaceNotification.saveNewNotification(payload);
    }

    @Override
    public Integer changeNotificationSettingsStatus(ChangeNotificationSettingsPayload payload) {
       payload.setIdCurrentUser(personService.getIdPersonFromSecurityContext());
       payload.setIdNotificationType(NotificationType.valueOf(payload.getNotificationType()).getNumber());
        return feignInterfaceNotification.changeNotificationSettingsStatus(payload);
    }

    @Override
    public DataNotificationSettings getSettings() {
        DataNotificationSettings dataNotificationSettings = new DataNotificationSettings();
        dataNotificationSettings.setUserId(personService.getIdPersonFromSecurityContext());
        dataNotificationSettings.setTime(OffsetDateTime.now());

        List<NotificationUnitSettings> settings = feignInterfaceNotification.getNotificationSettingsForCurrentUser(dataNotificationSettings.getUserId());
        dataNotificationSettings.setData(settings);


        return dataNotificationSettings;
    }
}
