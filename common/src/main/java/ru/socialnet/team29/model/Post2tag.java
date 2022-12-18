package ru.socialnet.team29.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Post2tag {
    private Integer id;
    private Integer postId;
    private Integer tagId;
}
