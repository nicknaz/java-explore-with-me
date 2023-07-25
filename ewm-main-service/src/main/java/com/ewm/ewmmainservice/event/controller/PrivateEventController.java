package com.ewm.ewmmainservice.event.controller;

import com.ewm.ewmmainservice.event.dto.EventFullDto;
import com.ewm.ewmmainservice.event.dto.EventRequestCreateDto;
import com.ewm.ewmmainservice.event.dto.EventShortDto;
import com.ewm.ewmmainservice.event.dto.UpdateEventUserRequestDto;
import com.ewm.ewmmainservice.event.service.priv.PrivateEventService;
import com.ewm.ewmmainservice.request.dto.model.RequestDto;
import com.ewm.ewmmainservice.request.dto.model.RequestStatusUpdateRequestDto;
import com.ewm.ewmmainservice.request.dto.model.RequestStatusUpdateResultDto;
import com.ewm.ewmmainservice.request.service.PrivateRequestCreatorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/users/{userId}/events")
public class PrivateEventController {
    private PrivateEventService eventService;
    private PrivateRequestCreatorService requestCreatorService;

    @Autowired
    public PrivateEventController(PrivateEventService eventService,
                                  PrivateRequestCreatorService requestCreatorService) {
        this.eventService = eventService;
        this.requestCreatorService = requestCreatorService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto create(@PathVariable Long userId,
                                @Valid @RequestBody EventRequestCreateDto eventRequestCreateDto) {
        EventFullDto eventFullDto = eventService.create(userId, eventRequestCreateDto);
        log.info("created event with id = {}", eventFullDto.getId());
        return eventFullDto;
    }

    @GetMapping("/{eventId}")
    public EventFullDto getEventByUser(@PathVariable Long userId,
                                       @PathVariable Long eventId) {
        log.info("get events by initiatorId = {} and eventId = {}", userId, eventId);
        return eventService.getEventByUser(userId, eventId);
    }

    @GetMapping
    public List<EventShortDto> getEventsListByUser(@PathVariable Long userId,
                                                   @RequestParam(defaultValue = "0") Integer from,
                                                   @RequestParam(defaultValue = "10") Integer size) {
        log.info("get events by initiatorId = {}", userId);
        return eventService.getEventsListByUser(userId, PageRequest.of(from / size, size));
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateEvent(@PathVariable Long userId,
                                    @PathVariable Long eventId,
                                    @Valid @RequestBody UpdateEventUserRequestDto updateEventUserRequestDto) {
        return eventService.patchEvent(userId, eventId, updateEventUserRequestDto);
    }

    @GetMapping("/{eventId}/requests")
    public List<RequestDto> getRequestsByCreator(@PathVariable Long userId,
                                                      @PathVariable Long eventId) {
        log.info("Get request by creator = {}", userId);
        return requestCreatorService.getRequestsByCreator(userId, eventId);
    }

    @PatchMapping("/{eventId}/requests")
    public RequestStatusUpdateResultDto updateRequests(@PathVariable Long userId,
                                                       @PathVariable Long eventId,
                                                       @RequestBody RequestStatusUpdateRequestDto requestStatusUpdateRequestDto) {
        return requestCreatorService.updateRequests(userId, eventId, requestStatusUpdateRequestDto);
    }

}
