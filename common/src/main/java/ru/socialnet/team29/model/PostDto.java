package ru.socialnet.team29.model;

import java.time.OffsetDateTime;
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
  private OffsetDateTime time;
  private OffsetDateTime timeChanged;
  private Integer authorId;
  private String title;
  private PostType type;
  private String postText;
  private Boolean isBlocked;
  private Boolean isDelete;
  private Integer commentsCount;
  private List<String> tags;
  private Integer likeAmount;
  private boolean myLike;
  private String imagePath;
  private OffsetDateTime publishDate;
}
