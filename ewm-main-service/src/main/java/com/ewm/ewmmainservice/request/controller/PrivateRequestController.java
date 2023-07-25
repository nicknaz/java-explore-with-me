package com.ewm.ewmmainservice.request.controller;

import com.ewm.ewmmainservice.request.dto.model.RequestDto;
import com.ewm.ewmmainservice.request.service.PrivateRequestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/users/{userId}/requests")
@Validated
public class PrivateRequestController {
    private PrivateRequestService requestService;

    @Autowired
    public PrivateRequestController(PrivateRequestService requestService) {
        this.requestService = requestService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RequestDto create(@PathVariable Long userId,
                            @RequestParam Long eventId) {
        RequestDto requestDto = requestService.create(userId, eventId);
        log.info("Create request with id = {}", requestDto.getId());
        return requestDto;
    }

    @GetMapping
    public List<RequestDto> getRequestsListByUser(@PathVariable Long userId) {
        log.info("Get requests list by user = {}", userId);
        return requestService.getRequestsListByUser(userId);
    }

    @PatchMapping("/{requestId}/cancel")
    public RequestDto cancel(@PathVariable Long userId,
                             @PathVariable Long requestId) {
        log.info("Cancel request with id = {} by user = {}", requestId, userId);
        return requestService.cancel(userId, requestId);
    }
}
