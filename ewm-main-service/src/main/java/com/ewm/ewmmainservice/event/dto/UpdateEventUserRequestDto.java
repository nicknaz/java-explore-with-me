package com.ewm.ewmmainservice.event.dto;

import com.ewm.ewmmainservice.event.model.EventState;
import com.ewm.ewmmainservice.event.model.Location;
import com.ewm.ewmmainservice.event.model.NewEventStateInitiator;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateEventUserRequestDto {
    @Size(min = 20, max = 2000)
    private String annotation;

    private Long category;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @FutureOrPresent
    private LocalDateTime eventDate;

    private Boolean paid;

    @Size(min = 3, max = 120)
    private String title;

    @Size(min = 20, max = 7000)
    private String description;

    private Location location;

    private Long participantLimit;

    private Boolean requestModeration;

    private NewEventStateInitiator stateAction;

    private EventState state;
}
