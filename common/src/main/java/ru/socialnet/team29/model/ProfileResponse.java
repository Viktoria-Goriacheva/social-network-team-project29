package ru.socialnet.team29.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;



@Data
@Builder
@AllArgsConstructor
public class ProfileResponse {
    private String error;
    private Long timestamp;
    @JsonProperty(value = "data")
    private Person person;
}
