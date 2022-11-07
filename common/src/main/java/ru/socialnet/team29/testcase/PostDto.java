package ru.socialnet.team29.testcase;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PostDto {
  private Integer id;
  private String name;
}
