package ru.socialnet.team29.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.socialnet.team29.answers.AddNewNotification;
import ru.socialnet.team29.answers.NotificationForFront;
import ru.socialnet.team29.interfaceDb.NotificationInterface;
import ru.socialnet.team29.payloads.AddNotificationPayload;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationInterface notificationService;


    @GetMapping("/notifications/count")
    public int getCountNotifications(@RequestParam("idPerson") Integer idPerson) {
        log.info("Получили запрос от core - Найти количество уведомлений пользователя => " + idPerson);
        return notificationService.getCountNotificationsById(idPerson);
    }

    @GetMapping("/notifications")
    public List<NotificationForFront> getAllNotificationsByPersonId(@RequestParam("idPerson") Integer idPerson) {
        log.info("Получили запрос от core - Найти все уведомления пользователя => " + idPerson);
        return notificationService.getAllNotificationsById(idPerson);
    }

    @PostMapping(value = "/notifications")
    public AddNewNotification saveNewNotification(@RequestBody AddNotificationPayload payload) {
        return notificationService.saveNewNotification(payload);
    }
}
