package ru.socialnet.team29.serviceInterface.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import ru.socialnet.team29.answers.AddNewNotification;
import ru.socialnet.team29.answers.NotificationForFront;
import ru.socialnet.team29.payloads.AddNotificationPayload;

import java.util.List;

@FeignClient(name = "dbNotification", url = "${server.db.port}")
public interface DBConnectionFeignInterfaceNotification {

    @GetMapping(value = "/notifications/count")
    Integer getCountNotification(@RequestParam Integer idPerson);
    @GetMapping(value = "/notifications")
    List<NotificationForFront> getAllNotificationsById(@RequestParam Integer idPerson);

    @PostMapping(value = "/notifications")
    AddNewNotification saveNewNotification(@RequestBody AddNotificationPayload payload);
}
