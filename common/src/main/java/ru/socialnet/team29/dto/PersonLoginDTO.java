package ru.socialnet.team29.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PersonLoginDTO {
    String email;
    String password;
}
