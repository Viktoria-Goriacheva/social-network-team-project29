package ru.socialnet.team29.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationCommon
{

    private Integer id;
    private int typeId;
    private LocalDateTime sentTime;
    private int personId;
    private int entityId;
    private String contact;
    private String content;

}
