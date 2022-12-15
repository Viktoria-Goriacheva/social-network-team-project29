package ru.socialnet.team29.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.socialnet.team29.answers.AddNewNotification;
import ru.socialnet.team29.answers.NotificationForFront;
import ru.socialnet.team29.interfaceDb.NotificationInterface;
import ru.socialnet.team29.interfaceDb.PersonInterfaceDB;
import ru.socialnet.team29.mappers.NotificationMapper;
import ru.socialnet.team29.model.NotificationCommon;
import ru.socialnet.team29.model.Person;
import ru.socialnet.team29.payloads.AddNotificationPayload;
import ru.socialnet.team29.repository.NotificationRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService implements NotificationInterface {
    private final NotificationRepository notificationRepository;
    private final PersonInterfaceDB personService;
    private final NotificationMapper mapper;


   @Override
    public Integer getCountNotificationsById(Integer id) {
        return notificationRepository.getCountNotificationByPersonId(id);
    }

    @Override
    public List<NotificationForFront> getAllNotificationsById(Integer idPerson) {
        List<NotificationCommon> notificationCommonList = notificationRepository.getAllNotificationsByPersonsId(idPerson);
        List<NotificationForFront> result = new ArrayList<>();
        for (NotificationCommon notification : notificationCommonList) {
            Person person = personService.getPersonByEmail(notification.getContact());
            result.add(mapper.notificationCommonToNotificationForFront(notification, person));
        }
        return result;
    }

    @Override
    public AddNewNotification saveNewNotification(AddNotificationPayload payload) {
        String contact = personService.findEmailByPersonId(payload.getAuthorId());
        NotificationCommon notificationBeforeSave = mapper.addNotificationPayloadToNotificationCommon(payload, contact);
        NotificationCommon notificationAfterSaved = notificationRepository.addNewNotification(notificationBeforeSave);
        return mapper.notificationCommonToAddNewNotification(notificationAfterSaved, payload);
    }

}
