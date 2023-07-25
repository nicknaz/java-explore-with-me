package com.ewm.ewmmainservice.event.controller;

import com.ewm.ewmmainservice.event.dto.EventFullDto;
import com.ewm.ewmmainservice.event.dto.UpdateEventAdminRequestDto;
import com.ewm.ewmmainservice.event.model.EventState;
import com.ewm.ewmmainservice.event.service.admin.AdminEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/admin/events")
public class AdminEventController {
    private AdminEventService eventService;

    @Autowired
    public AdminEventController(AdminEventService eventService) {
        this.eventService = eventService;
    }

    @PatchMapping("/{eventId}")
    public EventFullDto patchEvent(@PathVariable(name = "eventId") Long eventId,
                                    @Valid @RequestBody UpdateEventAdminRequestDto updateEventAdminRequestDto) {
        return eventService.patchEvent(eventId, updateEventAdminRequestDto);

    }

    @GetMapping
    public List<EventFullDto> getEventsList(@RequestParam(required = false) List<Long> users,
                                        @RequestParam(required = false) EventState states,
                                        @RequestParam(required = false) List<Long> categories,
                                        @RequestParam(required = false) String rangeStart,
                                        @RequestParam(required = false) String rangeEnd,
                                        @RequestParam(defaultValue = "0") Integer from,
                                        @RequestParam(defaultValue = "10") Integer size) {
        return eventService.getEventsList(users, states, categories, rangeStart, rangeEnd, PageRequest.of(from / size, size));
    }
}
