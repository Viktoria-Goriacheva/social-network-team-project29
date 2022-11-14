package ru.socialnet.team29.exception;

import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    public String error;
    public String description;
}
