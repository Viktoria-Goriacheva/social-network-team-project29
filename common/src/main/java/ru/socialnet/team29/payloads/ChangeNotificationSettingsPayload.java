package ru.socialnet.team29.payloads;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ChangeNotificationSettingsPayload
{
    @JsonProperty(value = "notification_type")
    private String notificationType;
    private Boolean enable;
    private int idCurrentUser;
    private int idNotificationType;

}
