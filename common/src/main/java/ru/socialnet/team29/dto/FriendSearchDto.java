package ru.socialnet.team29.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class FriendSearchDto {
    @Builder.Default
    private List<Integer> ids = new ArrayList<>();
    @Builder.Default
    private String firstName = "";
    @Builder.Default
    private String birthDateFrom = "";
    @Builder.Default
    private String birthDateTo = "";
    @Builder.Default
    private String city = "";
    @Builder.Default
    private String country = "";
    @Builder.Default
    private Integer ageTo = 0;
    @Builder.Default
    private Integer ageFrom = 0;
    @Builder.Default
    private String statusCode = "NONE";
}
