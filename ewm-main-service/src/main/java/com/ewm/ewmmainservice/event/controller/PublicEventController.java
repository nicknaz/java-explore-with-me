package com.ewm.ewmmainservice.event.controller;


import com.ewm.ewmmainservice.event.dto.EventFullDto;
import com.ewm.ewmmainservice.event.model.SortType;
import com.ewm.ewmmainservice.event.service.publ.PublicEventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/events")
public class PublicEventController {
    private PublicEventService eventService;

    @Autowired
    public PublicEventController(PublicEventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public List<EventFullDto> getEventsList(@RequestParam(required = false) String text,
                                                        @RequestParam(required = false) List<Long> categories,
                                                        @RequestParam(required = false) Boolean paid,
                                                        @RequestParam(required = false) String rangeStart,
                                                        @RequestParam(required = false) String rangeEnd,
                                                        @RequestParam(required = false) Boolean onlyAvailable,
                                                        @RequestParam(required = false) SortType sort,
                                                        @RequestParam(defaultValue = "0") Integer from,
                                                        @RequestParam(defaultValue = "10") Integer size,
                                                        HttpServletRequest request) {
        log.info("public get events");
        return eventService.getEventsList(text,
                categories,
                paid,
                rangeStart,
                rangeEnd,
                onlyAvailable,
                sort,
                PageRequest.of(from / size, size),
                request);
    }

    @GetMapping("/{id}")
    public EventFullDto getEvent(@PathVariable Long id, HttpServletRequest request) {
        return eventService.getEvent(id, request);
    }
}
