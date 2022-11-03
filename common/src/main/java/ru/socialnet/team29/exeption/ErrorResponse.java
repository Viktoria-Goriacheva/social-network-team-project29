package ru.socialnet.team29.exeption;

import lombok.*;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ErrorResponse {
    public String error;
    public String description;
}
