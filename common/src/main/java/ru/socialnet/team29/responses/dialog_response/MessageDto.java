package ru.socialnet.team29.responses.dialog_response;

import lombok.*;
import org.springframework.stereotype.Component;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Component
public class MessageDto {
    private Long id;
    private Long time;
    private Long authorId;
    private Long recipientId;
    private String messageText;
    private String readStatus;
    @Builder.Default
    private Integer count = 1;
}
