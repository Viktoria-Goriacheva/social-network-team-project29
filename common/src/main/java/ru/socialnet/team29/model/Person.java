package ru.socialnet.team29.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

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
    private String statusCode;
    @JsonProperty(value = "firstName")
    private String firstName;
    @JsonProperty(value = "lastName")
    private String lastName;
    @JsonProperty(value = "regDate")
    private LocalDateTime regDate;
    @JsonProperty(value = "birthDate")
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
    private String password;
}
