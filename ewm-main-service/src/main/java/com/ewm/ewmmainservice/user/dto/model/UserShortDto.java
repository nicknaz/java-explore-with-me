package com.ewm.ewmmainservice.user.dto.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserShortDto {
    private long id;

    private String name;
}
