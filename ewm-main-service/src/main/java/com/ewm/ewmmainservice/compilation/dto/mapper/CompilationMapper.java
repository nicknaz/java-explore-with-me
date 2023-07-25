package com.ewm.ewmmainservice.compilation.dto.mapper;

import com.ewm.ewmmainservice.compilation.dto.CompilationDto;
import com.ewm.ewmmainservice.compilation.dto.CompilationRequestDto;
import com.ewm.ewmmainservice.compilation.model.Compilation;
import com.ewm.ewmmainservice.event.dto.mapper.EventMapper;
import com.ewm.ewmmainservice.event.model.Event;

import java.util.List;
import java.util.stream.Collectors;

public class CompilationMapper {
    public static Compilation toCompilation(CompilationRequestDto compilationRequestDto, List<Event> events) {
        return Compilation.builder()
                .pinned(compilationRequestDto.getPinned() != null ? compilationRequestDto.getPinned() : false)
                .title(compilationRequestDto.getTitle())
                .events(events)
                .build();
    }

    public static CompilationDto toCompilationDto(Compilation compilation) {
        return CompilationDto.builder()
                .id(compilation.getId())
                .pinned(compilation.getPinned())
                .title(compilation.getTitle())
                .events(compilation.getEvents() != null ? compilation.getEvents()
                        .stream()
                        .map(EventMapper::toEventShortDto)
                        .collect(Collectors.toList()) : null)
                .build();
    }
}
