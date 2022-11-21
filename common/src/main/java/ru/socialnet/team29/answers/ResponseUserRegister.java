package ru.socialnet.team29.answers;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import ru.socialnet.team29.answers_interface.CommonAnswer;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseUserRegister implements CommonAnswer {

    private String error;
    private Long timestamp;
    @JsonProperty(value = "data")
    private MessageAnswer messageAnswer;


}
