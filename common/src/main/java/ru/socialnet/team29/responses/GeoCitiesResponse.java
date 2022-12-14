package ru.socialnet.team29.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GeoCitiesResponse {
    private Integer id;
    private String title;
    private Integer countryId;
}
