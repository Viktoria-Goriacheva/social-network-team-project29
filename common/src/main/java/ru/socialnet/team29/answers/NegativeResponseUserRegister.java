package ru.socialnet.team29.answers;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import ru.socialnet.team29.answers_interface.CommonAnswer;

@Data
public class NegativeResponseUserRegister implements CommonAnswer
{

    private String error;
    @JsonProperty(value = "error_description")
    private String errorDescription;
}
