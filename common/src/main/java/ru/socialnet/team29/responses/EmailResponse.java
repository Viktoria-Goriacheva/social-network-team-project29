package ru.socialnet.team29.responses;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EmailResponse {
    String temp;
    String email;
}
