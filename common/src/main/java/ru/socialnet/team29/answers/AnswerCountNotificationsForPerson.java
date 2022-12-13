package ru.socialnet.team29.answers;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnswerCountNotificationsForPerson {
    private Long timestamp;

    @JsonProperty(value = "data")
    private CountNotifications countNotifications;


}
