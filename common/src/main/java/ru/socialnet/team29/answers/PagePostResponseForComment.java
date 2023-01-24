package ru.socialnet.team29.answers;

import lombok.*;
import ru.socialnet.team29.dto.CommentDto;
import ru.socialnet.team29.model.PageableObject;
import ru.socialnet.team29.model.Sort;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PagePostResponseForComment  {
    private Integer totalElements;
    private Integer totalPages;
    private Integer number;
    private Integer size;
    private List<CommentDto> content;
    private List<Sort> sort;
    private boolean first;
    private boolean last;
    private Integer numberOfElements;
    private PageableObject pageableObject;
    private boolean empty;

}