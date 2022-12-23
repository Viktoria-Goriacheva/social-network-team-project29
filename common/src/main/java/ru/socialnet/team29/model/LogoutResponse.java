package ru.socialnet.team29.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@Builder
public class LogoutResponse {
    public String error;
    public OffsetDateTime timestamp;
    public OffsetDateTime data;
}
