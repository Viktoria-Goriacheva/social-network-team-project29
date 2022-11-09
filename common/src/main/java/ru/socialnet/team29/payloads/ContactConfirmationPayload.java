package ru.socialnet.team29.payloads;

import lombok.Data;

@Data
public class ContactConfirmationPayload {

    private String code;
    private String email;
    private String firstName;
    private String lastName;
    private String passwd1;
    private String passwd2;
}
