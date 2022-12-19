package ru.socialnet.team29.payloads;

import lombok.Data;
import lombok.Getter;

import java.time.OffsetDateTime;

@Data
@Getter
public class AccountUpdatePayload {
    private String firstName;
    private String lastName;
    private String phone;
    private OffsetDateTime birthDate;
    private String about;
    private String country;
    private String city;
    private String photo;
}
