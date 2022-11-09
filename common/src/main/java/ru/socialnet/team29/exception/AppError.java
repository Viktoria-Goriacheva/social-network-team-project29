package ru.socialnet.team29.exception;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AppError {
    private int statusCode;
    private String message;

    public AppError(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }
}