package ru.socialnet.team29.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import ru.socialnet.team29.model.enums.CommentType;

import java.time.OffsetDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CommentDto {

    private Integer id;
    private String commentType;
    private OffsetDateTime time;

    private Integer authorId;
    private Integer parentId;
    private String commentText;
    private Integer postId;
    private Boolean isBlocked;
    @JsonProperty(value = "likeAmount")
    private Integer likes;
    private Boolean myLike;
    private List<CommentDto> subComments;
}
