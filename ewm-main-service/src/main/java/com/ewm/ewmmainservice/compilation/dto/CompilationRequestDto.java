package com.ewm.ewmmainservice.compilation.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@Builder
public class CompilationRequestDto {
    @NotBlank
    @Size(max = 50)
    private String title;

    private Boolean pinned;

    private List<Long> events;

}
