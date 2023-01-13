package ru.socialnet.team29.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.socialnet.team29.answers.*;
import ru.socialnet.team29.model.enums.NotificationType;
import ru.socialnet.team29.payloads.AddNotificationPayload;
import ru.socialnet.team29.payloads.ChangeNotificationSettingsPayload;
import ru.socialnet.team29.service.PersonServiceImpl;
import ru.socialnet.team29.serviceInterface.NotificationService;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@Slf4j
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;
    private final PersonServiceImpl personService;


    @GetMapping(value = "/notifications/count")
    public ResponseEntity<AnswerCountNotificationsForPerson> getCountOfNotifications() {
        log.info("Получаем запрос от фронта на количество уведомлений для  " + SecurityContextHolder.getContext().getAuthentication().getName());
        int id = personService.getIdPersonFromSecurityContext();

        return new ResponseEntity<>(new AnswerCountNotificationsForPerson(System.currentTimeMillis(),
                new CountNotifications(notificationService.getCountAllNotifications(id))),
                HttpStatus.OK);
    }

    @GetMapping(value = "/notifications")
    public ResponseEntity<AnswerListAllNotificationsForPerson> getNotificationsByIdPerson() {
        log.info("Получаем запрос от фронта на вывод всех уведомлений для  " + SecurityContextHolder.getContext().getAuthentication().getName());
        int id = personService.getIdPersonFromSecurityContext();
        List<NotificationForFront> listNotifications = notificationService.getAllNotificationsForPerson(id);
        return new ResponseEntity<>(new AnswerListAllNotificationsForPerson(LocalDateTime.now(), listNotifications), HttpStatus.OK);
    }

    @PostMapping(value = "/notifications")
    public ResponseEntity<AddNewNotification> getCountOfNotifications(@RequestBody AddNotificationPayload payload) {
        log.info("Получаем запрос от фронта на добавление нового уведомления");
        payload.setNotificationTypeId(NotificationType.valueOf(payload.getNotificationType()).getNumber());
        return new ResponseEntity<>(notificationService.addNewNotification(payload),
                HttpStatus.OK);
    }

    @GetMapping(value = "/notifications/settings")
    public ResponseEntity<DataNotificationSettings> getNotificationsSettings() {
        DataNotificationSettings dataNotificationSettings = notificationService.getSettings();
        return new ResponseEntity<>(dataNotificationSettings, HttpStatus.OK);
    }

    @PutMapping(value = "/notifications/settings")
    public ResponseEntity<Integer> changeSettings(@RequestBody ChangeNotificationSettingsPayload payload){
       int res = notificationService.changeNotificationSettingsStatus(payload);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

}

