package com.ewm.ewmmainservice.event.dto.mapper;

import com.ewm.ewmmainservice.category.dto.mapper.CategoryMapper;
import com.ewm.ewmmainservice.category.model.Category;
import com.ewm.ewmmainservice.event.dto.EventFullDto;
import com.ewm.ewmmainservice.event.dto.EventRequestCreateDto;
import com.ewm.ewmmainservice.event.dto.EventShortDto;
import com.ewm.ewmmainservice.event.model.Event;
import com.ewm.ewmmainservice.event.model.EventState;
import com.ewm.ewmmainservice.event.model.Location;
import com.ewm.ewmmainservice.user.dto.mapper.UserMapper;
import com.ewm.ewmmainservice.user.model.User;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;

@UtilityClass
public class EventMapper {
    public static Event toEventFromNewEventDto(EventRequestCreateDto dto, Location location, Category category, User user) {
        return Event.builder()
                .annotation(dto.getAnnotation())
                .category(category)
                .createdOn(LocalDateTime.now())
                .paid(dto.getPaid() != null ? dto.getPaid() : false)
                .participantLimit(dto.getParticipantLimit() != null ? dto.getParticipantLimit() : 0)
                .requestModeration(dto.getRequestModeration() != null ? dto.getRequestModeration() : true)
                .description(dto.getDescription())
                .eventDate(dto.getEventDate())
                .confirmedRequest(0)
                .initiator(user)
                .location(location)
                .views(0L)
                .state(EventState.PENDING)
                .title(dto.getTitle())
                .build();
    }

    public static EventFullDto toEventFullDto(Event event) {
        return EventFullDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(CategoryMapper.toCategoryDto(event.getCategory()))
                .confirmedRequests(event.getConfirmedRequest())
                .initiator(UserMapper.toUserShortDto(event.getInitiator()))
                .publishedOn(event.getPublishedOn())
                .createdOn(event.getCreatedOn())
                .description(event.getDescription())
                .eventDate(event.getEventDate())
                .requestModeration(event.getRequestModeration())
                .state(event.getState())
                .title(event.getTitle())
                .location(event.getLocation())
                .paid(event.getPaid())
                .participantLimit(event.getParticipantLimit())
                .views(event.getViews())
                .build();
    }

    public static EventShortDto toEventShortDto(Event event) {
        return EventShortDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(CategoryMapper.toCategoryDto(event.getCategory()))
                .confirmedRequests(event.getConfirmedRequest())
                .eventDate(event.getEventDate())
                .initiator(UserMapper.toUserShortDto(event.getInitiator()))
                .paid(event.getPaid())
                .title(event.getTitle())
                .views(event.getViews())
                .build();
    }
}
