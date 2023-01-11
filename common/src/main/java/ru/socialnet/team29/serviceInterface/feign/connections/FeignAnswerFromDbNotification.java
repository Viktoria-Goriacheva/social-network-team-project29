package ru.socialnet.team29.serviceInterface.feign.connections;

import org.springframework.stereotype.Component;
import ru.socialnet.team29.answers.AddNewNotification;
import ru.socialnet.team29.answers.NotificationForFront;
import ru.socialnet.team29.answers.NotificationUnitSettings;
import ru.socialnet.team29.payloads.AddNotificationPayload;
import ru.socialnet.team29.payloads.ChangeNotificationSettingsPayload;
import ru.socialnet.team29.serviceInterface.feign.DBConnectionFeignInterfaceNotification;


import java.util.List;

@Component
public class FeignAnswerFromDbNotification implements DBConnectionFeignInterfaceNotification {


    @Override
    public Integer getCountNotification(Integer idPerson) {
        return null;
    }

    @Override
    public List<NotificationForFront> getAllNotificationsById(Integer idPerson) {
        return null;
    }

    @Override
    public AddNewNotification saveNewNotification(AddNotificationPayload payload) {
        return null;
    }

    @Override
    public Integer changeNotificationSettingsStatus(ChangeNotificationSettingsPayload payload) {
        return null;
    }

    @Override
    public List<NotificationUnitSettings> getNotificationSettingsForCurrentUser(Integer userId) {
        return null;
    }
}
