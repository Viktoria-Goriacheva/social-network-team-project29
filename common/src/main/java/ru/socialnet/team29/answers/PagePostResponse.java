package ru.socialnet.team29.answers;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.socialnet.team29.model.PageableObject;
import ru.socialnet.team29.model.PostDto;
import ru.socialnet.team29.model.Sort;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PagePostResponse  {

  private Integer totalElements;
  private Integer totalPages;
  private Integer number;
  private Integer size;
  private List<PostDto> content;
  private List<Sort> sort;
  private boolean first;
  private boolean last;
  private Integer numberOfElements;
  private PageableObject pageableObject;
  private boolean empty;

}






