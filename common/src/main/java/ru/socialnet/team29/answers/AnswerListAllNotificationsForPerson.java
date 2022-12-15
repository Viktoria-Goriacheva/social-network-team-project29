package ru.socialnet.team29.answers;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnswerListAllNotificationsForPerson {

    private LocalDateTime timeStamp;

    @JsonProperty(value = "data")
    private List<NotificationForFront> countNotifications;

}
