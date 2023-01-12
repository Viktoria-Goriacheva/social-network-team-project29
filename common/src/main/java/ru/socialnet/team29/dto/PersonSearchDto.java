package ru.socialnet.team29.dto;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class PersonSearchDto {
    private String firstName;
    @Builder.Default
    private String birthDateFrom = "1900-01-01T00:00:00+00:00";
    @Builder.Default
    private String birthDateTo = "3000-01-01T00:00:00+00:00";
    private String city;
    private String country;
    private Integer ageTo;
    private Integer ageFrom;
    @Builder.Default
    private String statusCode = "NONE";
    @Builder.Default
    private Integer size = 20;
    @Builder.Default
    private Integer page = 1;
}
