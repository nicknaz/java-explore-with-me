package com.ewm.ewmmainservice.compilation.dto;

import com.ewm.ewmmainservice.event.dto.EventShortDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class CompilationDto {
    private Long id;

    private Boolean pinned;

    private String title;

    private List<EventShortDto> events;
}
