package ru.socialnet.team29.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.socialnet.team29.model.enums.NotificationType;
import ru.socialnet.team29.payloads.AddNotificationPayload;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class LaunchCheckBirthdate {
    private final PersonService personService;
    private final FriendService friendService;
    private final NotificationService notificationService;


    @Scheduled(cron = "${interval-in-cron}")
    public void refreshBirthdateFriends() {
        log.info("Рассылка уведомлений о днях рождения сегодня.");
        List<Integer> listUsersBirthdateToday = personService.getPersonBirthDate();
        if (listUsersBirthdateToday.size() != 0) {
            for (Integer author : listUsersBirthdateToday) {
                makeNotificationOnBirthDateToAllUsers(author);
            }
        }
    }

    private void makeNotificationOnBirthDateToAllUsers(Integer author) {
        personService.getAllPersonIds()
                .forEach(e -> {
                    if (!Objects.equals(e, author)) {
                        AddNotificationPayload payload = AddNotificationPayload.builder()
                                .authorId(String.valueOf(author))
                                .userId(String.valueOf(e))
                                .notificationType(NotificationType.FRIEND_BIRTHDAY.getValue())
                                .NotificationTypeId(NotificationType.FRIEND_BIRTHDAY.getNumber())
                                .content("У меня сегодня день рождения. Жду поздравлений, если не жалко")
                                .build();
                        notificationService.saveNewNotification(payload);
                    }
                });
    }

    private void makeNotificationOnBirthDateToFriends(Integer author) {
        friendService.getIdsFriendsById(author)
                .forEach(e -> {
                    AddNotificationPayload payload = AddNotificationPayload.builder()
                            .authorId(String.valueOf(author))
                            .userId(String.valueOf(e))
                            .notificationType(NotificationType.FRIEND_BIRTHDAY.getValue())
                            .NotificationTypeId(NotificationType.FRIEND_BIRTHDAY.getNumber())
                            .content("У меня сегодня день рождения. Жду поздравлений, если не жалко")
                            .build();
                    notificationService.saveNewNotification(payload);
                });
    }

}
