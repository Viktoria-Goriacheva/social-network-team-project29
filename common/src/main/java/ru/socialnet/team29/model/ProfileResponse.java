package ru.socialnet.team29.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;



@Data
@Builder
@AllArgsConstructor
public class ProfileResponse {
    public String error;
    public LocalDateTime timestamp;
    public Person person;
}
