package ru.socialnet.team29.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;



@Data
@Builder
@AllArgsConstructor
public class ProfileResponse {
    private String error;
    private Long timestamp;
    @JsonProperty(value = "data")
    private Person person;
}
