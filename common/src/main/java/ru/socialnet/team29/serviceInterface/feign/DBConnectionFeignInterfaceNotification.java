package ru.socialnet.team29.serviceInterface.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import ru.socialnet.team29.answers.AddNewNotification;
import ru.socialnet.team29.answers.NotificationForFront;
import ru.socialnet.team29.answers.NotificationUnitSettings;
import ru.socialnet.team29.payloads.AddNotificationPayload;
import ru.socialnet.team29.payloads.ChangeNotificationSettingsPayload;

import java.util.List;

@FeignClient(name = "dbNotification", url = "${server.db.port}")
public interface DBConnectionFeignInterfaceNotification {

    @GetMapping(value = "/notifications/count")
    Integer getCountNotification(@RequestParam Integer idPerson);

    @GetMapping(value = "/notifications")
    List<NotificationForFront> getAllNotificationsById(@RequestParam Integer idPerson);

    @PostMapping(value = "/notifications")
    AddNewNotification saveNewNotification(@RequestBody AddNotificationPayload payload);

    @PutMapping("/notifications/settings")
    Integer changeNotificationSettingsStatus(@RequestBody ChangeNotificationSettingsPayload payload);

    @GetMapping("/notifications/settings")
    List<NotificationUnitSettings> getNotificationSettingsForCurrentUser(@RequestParam Integer userId);
}
