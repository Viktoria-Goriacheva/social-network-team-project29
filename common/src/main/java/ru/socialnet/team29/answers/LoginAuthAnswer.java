package ru.socialnet.team29.answers;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.socialnet.team29.model.Person;

@Data
@AllArgsConstructor
public class LoginAuthAnswer
{
    private String error;
    @JsonProperty(value = "timestamp")
    private Long requestTime;
    @JsonProperty(value = "data")
    private Person person;

    @JsonProperty(value = "messages_permission")
    private String messagesPermission;
    @JsonProperty(value = "last_online_time")
    private Long lastOnlineTime;
    @JsonProperty(value = "is_blocked")
    private Boolean isBlocked;
    private String token;

}
