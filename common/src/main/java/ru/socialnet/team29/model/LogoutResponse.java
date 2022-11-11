package ru.socialnet.team29.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@Builder
public class LogoutResponse {
    public String error;
    public LocalDateTime timestamp;
    public LocalDateTime data;
}
