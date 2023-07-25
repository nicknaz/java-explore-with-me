package com.ewm.ewmmainservice.user.dto.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@Builder
public class UserDto {
    private long id;

    @NotNull
    @NotBlank
    @Size(min = 2, max = 250)
    private String name;

    @Email
    @NotNull
    @NotBlank
    @Size(min = 6, max = 254)
    private String email;
}
