package ru.socialnet.team29.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.socialnet.team29.model.enums.BlockStatus;
import ru.socialnet.team29.model.enums.MessagePermission;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Person {
    public Long id;
    public String firstName;
    public String lastName;
    public LocalDateTime registrationDate;
    public LocalDateTime birthDate;
    public String email;
    public String phone;
    @JsonProperty(value = "confirmation-code")
    public String confirmationCode;
    public String password;
    public String photo;
    public String about;
    public City city;
    public Country country;
    public MessagePermission messagesPermission;
    public LocalDateTime lastOnlineTime;
    public BlockStatus isBlocked;
}
