package ru.socialnet.team29.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.socialnet.team29.model.enums.BlockStatus;
import ru.socialnet.team29.model.enums.MessagePermission;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Person {
    private Integer id;
    @JsonProperty(value = "first_name")
    private String firstName;
    @JsonProperty(value = "last_name")
    private String lastName;
    @JsonProperty(value = "reg_date")
    private Long registrationDate;
    @JsonProperty(value = "birth_date")
    private Long birthDate;

    private String email;
    private String phone;
    @JsonProperty(value = "confirmation-code")
    private String confirmationCode;
    private String password;
    private String photo;
    private String about;
    private City city;
    private Country country;

}
