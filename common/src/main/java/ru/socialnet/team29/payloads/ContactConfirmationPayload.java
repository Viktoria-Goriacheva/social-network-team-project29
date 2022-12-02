package ru.socialnet.team29.payloads;

import lombok.Data;

@Data
public class ContactConfirmationPayload {
    private String email;
    private String firstName;
    private String lastName;
    private String password1;
    private String password2;
    private String token;
    private String code;
}
