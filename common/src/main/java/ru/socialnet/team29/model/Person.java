package ru.socialnet.team29.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Person {

    private Integer id;
    private String email;
    private String phone;
    private String photo;
    private String about;
    private String city;
    private String country;
    private String token;
    private String password;
    private String statusCode;
    private String firstName;
    private String lastName;
    private OffsetDateTime regDate;
    private OffsetDateTime birthDate;
    private String messagesPermission;
    private OffsetDateTime lastOnlineTime;
    private Boolean isOnline;
    private Boolean isBlocked;
    private Boolean isDeleted;

    private String photoId;
    private String photoName;
    private String role;
    private OffsetDateTime createdOn;
    private OffsetDateTime updatedOn;
}
