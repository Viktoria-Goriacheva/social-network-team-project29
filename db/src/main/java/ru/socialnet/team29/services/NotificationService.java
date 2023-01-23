package ru.socialnet.team29.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.socialnet.team29.answers.AddNewNotification;
import ru.socialnet.team29.answers.NotificationForFront;
import ru.socialnet.team29.answers.NotificationUnitSettings;
import ru.socialnet.team29.domain.tables.records.PersonToNotificationSettingsRecord;
import ru.socialnet.team29.exception.DataBaseException;

import ru.socialnet.team29.interfaceDb.NotificationInterface;
import ru.socialnet.team29.interfaceDb.PersonInterfaceDB;
import ru.socialnet.team29.mappers.NotificationMapper;
import ru.socialnet.team29.model.NotificationCommon;
import ru.socialnet.team29.model.Person;
import ru.socialnet.team29.model.enums.NotificationType;
import ru.socialnet.team29.payloads.AddNotificationPayload;
import ru.socialnet.team29.payloads.ChangeNotificationSettingsPayload;
import ru.socialnet.team29.repository.NotificationRepository;
import ru.socialnet.team29.repository.NotificationSettingsRepository;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService implements NotificationInterface {
    private final NotificationRepository notificationRepository;
    private final PersonInterfaceDB personService;
    private final NotificationSettingsRepository notificationSettingsRepository;
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

    @Override
    public Integer changeNotificationSettingsStatus(ChangeNotificationSettingsPayload payload) throws DataBaseException {
        PersonToNotificationSettingsRecord record = new PersonToNotificationSettingsRecord();
        record.setUserId(payload.getIdCurrentUser());
        record.setNotificationTypeId(payload.getIdNotificationType());

        int result = payload.getEnable() ? notificationSettingsRepository.insert(record) :
                notificationSettingsRepository.delete(payload.getIdCurrentUser(), payload.getIdNotificationType());
        if (result == 1) {
            return 1;
        } else {
            throw new DataBaseException(this.getClass().getSimpleName(), "Save was not successfully");
        }

    }

    @Override
    public List<NotificationUnitSettings> getAllNotificationsSettingsByUserId(Integer userId) throws DataBaseException {
        int sizeListNotificationType = NotificationType.values().length;
        List<NotificationUnitSettings> unitSettingsList = new LinkedList<>();
        List<PersonToNotificationSettingsRecord> settings;
        try {
            settings = notificationSettingsRepository.getNotificationSettingsForCurrentUser(userId);
        } catch (Exception ex) {
            throw new DataBaseException(ex, this.getClass().getSimpleName(), "Something wrong in dataBase during fetching notificationSettingsForCurrentUser");
        }

        for (int i = 1; i <= sizeListNotificationType; i++) {
            NotificationUnitSettings unitSettings = new NotificationUnitSettings();
            unitSettings.setNotificationType(NotificationType.getTypeEnum(i));
            unitSettings.setEnable(false);
            unitSettingsList.add(unitSettings);
        }

        for (PersonToNotificationSettingsRecord item : settings) {
            unitSettingsList.get(item.getNotificationTypeId() - 1).setEnable(true);
        }

        return unitSettingsList;
    }

}
