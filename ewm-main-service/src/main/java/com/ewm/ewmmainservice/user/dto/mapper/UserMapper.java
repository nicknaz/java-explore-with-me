package com.ewm.ewmmainservice.user.dto.mapper;

import com.ewm.ewmmainservice.user.dto.model.UserDto;
import com.ewm.ewmmainservice.user.dto.model.UserShortDto;
import com.ewm.ewmmainservice.user.model.User;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UserMapper {
    public static UserDto toUserDto(User newUser) {
        return UserDto.builder()
                .id(newUser.getId())
                .name(newUser.getName())
                .email(newUser.getEmail())
                .build();
    }

    public static UserShortDto toUserShortDto(User newUser) {
        return UserShortDto.builder()
                .id(newUser.getId())
                .name(newUser.getName())
                .build();
    }

    public static User toUser(UserDto dto) {
        return User.builder()
                .id(dto.getId())
                .name(dto.getName())
                .email(dto.getEmail())
                .build();
    }
}
