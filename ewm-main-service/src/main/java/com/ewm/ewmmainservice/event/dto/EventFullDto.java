package com.ewm.ewmmainservice.event.dto;

import com.ewm.ewmmainservice.category.dto.model.CategoryDto;
import com.ewm.ewmmainservice.event.model.EventState;
import com.ewm.ewmmainservice.event.model.Location;
import com.ewm.ewmmainservice.user.dto.model.UserShortDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class EventFullDto {
    private Long id;

    private String annotation;

    private CategoryDto category;

    private Integer confirmedRequests;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    private UserShortDto initiator;

    private Boolean paid;

    private String title;

    private Long views;

    private String description;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdOn;

    private Location location;

    private Long participantLimit;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime publishedOn;

    private Boolean requestModeration;

    private EventState state;

}
