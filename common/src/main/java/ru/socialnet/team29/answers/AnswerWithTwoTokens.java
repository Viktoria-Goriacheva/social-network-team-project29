package ru.socialnet.team29.answers;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AnswerWithTwoTokens {

    private String accessToken;
    private String refreshToken;
}
