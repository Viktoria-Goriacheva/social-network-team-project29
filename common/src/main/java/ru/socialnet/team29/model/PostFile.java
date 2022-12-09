package ru.socialnet.team29.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PostFile {
    private Integer id;
    private Integer postId;
    private String imagePath;
    private String name;
}
