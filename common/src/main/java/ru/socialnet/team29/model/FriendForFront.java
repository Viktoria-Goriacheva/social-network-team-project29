package ru.socialnet.team29.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FriendForFront {
    private Integer id;
    private OffsetDateTime birthDate;
    private String city;
    private String country;
    private String firstName;
    private Boolean isOnline;
    private String lastName;
    private String photo;
    private String statusCode;
}
