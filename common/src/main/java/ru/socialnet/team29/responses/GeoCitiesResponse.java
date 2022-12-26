package ru.socialnet.team29.responses;

import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GeoCitiesResponse {
    private Integer id;
    @JsonSetter("name")
    private String title;
    @JsonSetter("parent_id")
    private Integer countryId;

}
