package com.ewm.ewmmainservice.request.dto.mapper;

import com.ewm.ewmmainservice.request.dto.model.RequestDto;
import com.ewm.ewmmainservice.request.model.Request;

public class RequestMapper {
    public static RequestDto toRequestDto(Request request) {
        return RequestDto.builder()
                .id(request.getId())
                .requester(request.getRequester().getId())
                .created(request.getCreated())
                .status(request.getStatus())
                .event(request.getEvent().getId())
                .build();
    }
}
