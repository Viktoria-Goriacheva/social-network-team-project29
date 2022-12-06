package ru.socialnet.team29.model;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.socialnet.team29.model.enums.PostType;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PostDto {

  private Integer id;
  private LocalDateTime time;
  private LocalDateTime timeChanged;
  private Integer authorId;
  private String title;
  private PostType type;
  private String postText;
  private boolean isBlocked;
  private boolean isDelete;
  private Integer commentsCount;
  private List<String> tags;
  private Integer likeAmount;
  private boolean myLike;
  private String imagePath;
  private LocalDateTime publishDate;
}
