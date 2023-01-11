package ru.socialnet.team29.payloads;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountSearchPayload {
    private String author;
    private String firstName;
    private String lastName;
    private String city;
    private String country;
    private Integer ageFrom;
    private Integer ageTo;
    private Integer size;
    private Integer page;
}
