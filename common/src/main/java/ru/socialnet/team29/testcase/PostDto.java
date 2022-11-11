package ru.socialnet.team29.testcase;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class PostDto {
  private Integer id;
  private String name;
}
