package ru.socialnet.team29.answers;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class DataNotificationSettings {

    private Integer userId;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private OffsetDateTime time;

    private List<NotificationUnitSettings> data = new ArrayList<>();

}
