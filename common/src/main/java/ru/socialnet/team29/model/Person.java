package ru.socialnet.team29.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.stereotype.Component;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Component
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
    private LocalDateTime regDate;
    private LocalDateTime birthDate;
    private String messagesPermission;
    private LocalDateTime lastOnlineTime;
    private Boolean isOnline;
    private Boolean isBlocked;
    private Boolean isDeleted;

    private String photoId;
    private String photoName;
    private String role;
    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;
}
