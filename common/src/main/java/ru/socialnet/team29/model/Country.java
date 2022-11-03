package ru.socialnet.team29.model;

import lombok.*;
import lombok.Data;


@Data
@Builder
@AllArgsConstructor
public class Country {
    public Long id;
    public String title;
}
