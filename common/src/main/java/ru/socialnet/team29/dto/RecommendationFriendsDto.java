package ru.socialnet.team29.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class RecommendationFriendsDto {
    private Integer id;
    @Builder.Default
    private String photo = "";
    @Builder.Default
    private String firstName = "";
    @Builder.Default
    private String lastName = "";
}
