package com.ewm.ewmmainservice.event.model;

import lombok.*;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Getter
@Setter
@Builder
public class SearchEventParams {
    private String text;
    private List<Long> categories;
    private Boolean paid;
    private String rangeStart;
    private String rangeEnd;
    private Boolean onlyAvailable;
    private SortType sort;
    private Pageable page;
    private HttpServletRequest request;
}
