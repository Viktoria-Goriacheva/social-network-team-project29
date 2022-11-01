package ru.socialnet.team29.model;

import lombok.Builder;
import lombok.Data;
import ru.socialnet.team29.model.enums.BlockStatus;
import ru.socialnet.team29.model.enums.MessagePermission;

import java.time.LocalDateTime;

@Data
@Builder
public class Person {
    public Long id;
    public String firstName;
    public String lastName;
    public LocalDateTime registrationDate;
    public LocalDateTime birthDate;
    public String email;
    public String phone;
    public String photo;
    public String about;
    public City city;
    public Country country;
    public MessagePermission messagesPermission;
    public LocalDateTime lastOnlineTime;
    public BlockStatus isBlocked;
}
