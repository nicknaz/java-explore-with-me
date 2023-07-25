package com.ewm.ewmmainservice.category.dto.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@Builder
public class CategoryDto {
    private Long id;
    @NotBlank
    @Size(min = 2, max = 50)
    private String name;
}
