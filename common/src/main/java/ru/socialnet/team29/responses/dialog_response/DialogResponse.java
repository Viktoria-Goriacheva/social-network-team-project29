package ru.socialnet.team29.responses.dialog_response;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class DialogResponse<T> {
    private Integer total;
    private Integer offset;
    private Integer perPage;
    private Long currentUserId;
    private Long timestamp;
    @Builder.Default
    private List<T> data = new ArrayList<> ();
}
