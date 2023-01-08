package ru.socialnet.team29.dto;

import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@Builder
public class PostLikeDto {
    private Integer personId;
    private Integer postId;
    @Builder.Default
    private Integer commentId = null;
    @Builder.Default
    private OffsetDateTime time = OffsetDateTime.now();
}
