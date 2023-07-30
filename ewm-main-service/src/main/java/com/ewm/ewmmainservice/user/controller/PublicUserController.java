package com.ewm.ewmmainservice.user.controller;

import com.ewm.ewmmainservice.user.service.PublicUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
@Slf4j
@RequestMapping(path = "/users")
public class PublicUserController {
    private PublicUserService userService;

    @Autowired
    public PublicUserController(PublicUserService userService) {
        this.userService = userService;
    }

    @PatchMapping("/subscribe/{id}")
    @ResponseStatus(HttpStatus.OK)
    public HttpStatus subscribe(@RequestHeader(name = "X-Sharer-User-Id") Long userId,
                             @PathVariable("id") Long trackedId) {
        log.info("{} subscribed to {}", userId, trackedId);
        userService.subscribe(userId ,trackedId);
        return HttpStatus.OK;
    }

}
