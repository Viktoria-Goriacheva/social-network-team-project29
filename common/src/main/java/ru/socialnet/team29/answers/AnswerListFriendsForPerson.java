package ru.socialnet.team29.answers;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.socialnet.team29.dto.PersonSearchDto;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class AnswerListFriendsForPerson<T> {
    @Builder.Default
    private List<T> content = new ArrayList<>();
    @Builder.Default
    private Boolean empty = true;
    @Builder.Default
    private Boolean first = true;
    @Builder.Default
    private Boolean last = true;
    @Builder.Default
    private Integer number = 0;
    @Builder.Default
    private Integer numberOfElements = 0;
    @Builder.Default
    private Integer size = 20;
    @Builder.Default
    private List<String> sort = new ArrayList<>();
    @Builder.Default
    private Integer totalElements = 0;
    @Builder.Default
    private Integer totalPages = 0;
    private FriendPageable pageable;
    private Integer id;

    @Data
    @AllArgsConstructor
    @Builder
    public static class FriendPageable {
        @Builder.Default
        private Integer offset = 0;
        @Builder.Default
        private Integer pageNumber = 0;
        @Builder.Default
        private Integer pageSize = 20;
        @Builder.Default
        private Boolean paged = true;
        @Builder.Default
        private List<String> sort = new ArrayList<>();
        @Builder.Default
        private Boolean unpaged = false;
    }
}
