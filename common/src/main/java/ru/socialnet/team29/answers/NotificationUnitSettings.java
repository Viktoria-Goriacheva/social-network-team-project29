package ru.socialnet.team29.answers;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class NotificationUnitSettings {

    @JsonProperty(value = "notification_type")
    private String notificationType;

    private Boolean enable;

}
