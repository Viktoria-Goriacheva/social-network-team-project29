package ru.socialnet.team29.payloads;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddNotificationPayload
{
    private String authorId; //Кто шлет уведомление
    private String userId;  //Кому шлет уведомление
    private String notificationType;
    private String content;
    private int NotificationTypeId;

}
