package com.ewm.ewmmainservice.user.controller;

import com.ewm.ewmmainservice.user.dto.model.UserDto;
import com.ewm.ewmmainservice.user.service.AdminUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@Validated
@Slf4j
@RequestMapping(path = "/admin/users")
public class AdminUserController {
    private AdminUserService userService;

    @Autowired
    public AdminUserController(AdminUserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<UserDto> findUsers(@RequestParam(required = false) List<Long> ids,
                                  @RequestParam(defaultValue = "0") int from,
                                  @RequestParam(defaultValue = "10") int size) {
        log.info("Get users with endpoint /admin/users");
        return userService.findUsers(ids, PageRequest.of(from / size, size));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto create(@Valid @RequestBody UserDto user) {
        UserDto newUser = userService.add(user);
        log.info("Create user with userId={}", newUser.getId());
        return newUser;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public HttpStatus delete(@PathVariable("id") Long userId) {
        log.info("Delete user by userId={}", userId);
        userService.delete(userId);
        return HttpStatus.NO_CONTENT;
    }

}
