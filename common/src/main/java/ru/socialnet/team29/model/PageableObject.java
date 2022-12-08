package ru.socialnet.team29.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PageableObject {

  public List<Sort> sort;
  public int pageNumber;
  public int pageSize;
  public int offset;
  public boolean unpaged;
  public boolean paged;

}
