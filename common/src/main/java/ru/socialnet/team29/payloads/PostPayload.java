package ru.socialnet.team29.payloads;

import lombok.Data;

import java.time.OffsetDateTime;
import java.util.ArrayList;

@Data
public class PostPayload {
    String title;
    String postText;
    ArrayList<String> tags;
    String imagePath;
    OffsetDateTime publishDate;
}
