package ru.socialnet.team29.model;

import lombok.Builder;
import lombok.Data;
import java.util.ArrayList;
@Data
@Builder
public class Page {
    private Integer page;
    private Integer size;
    private ArrayList<String> sort;
}
