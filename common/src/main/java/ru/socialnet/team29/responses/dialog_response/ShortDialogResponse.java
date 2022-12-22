package ru.socialnet.team29.responses.dialog_response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ShortDialogResponse<T> {
    @Builder.Default
    private String error = "";
    @Builder.Default
    private String error_description = "";
    private Long timestamp;
    private T data;
}
