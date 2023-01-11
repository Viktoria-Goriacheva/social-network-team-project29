package ru.socialnet.team29.responses;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RestPageImpl<T> extends PageImpl<T> {
    @JsonCreator(mode= JsonCreator.Mode.PROPERTIES)
    public RestPageImpl(List<T> content,
                        int number,
                        int size,
                        Long totalElements,
                        JsonNode pageable,
                        boolean last,
                        int totalPages,
                        JsonNode sort,
                        boolean first,
                        int numberOfElements) {
        super(content, PageRequest.of(number, size), totalElements);
    }

    public RestPageImpl(List<T> content, Pageable pageable, long total) {
        super(content, pageable, total);
    }

    public RestPageImpl(List<T> content) {
        super(content);
    }

    public RestPageImpl() {
        super(new ArrayList<>());
    }

}
