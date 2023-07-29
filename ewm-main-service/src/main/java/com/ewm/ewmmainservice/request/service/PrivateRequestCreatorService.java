package com.ewm.ewmmainservice.request.service;

import com.ewm.ewmmainservice.request.dto.model.RequestDto;
import com.ewm.ewmmainservice.request.dto.model.RequestStatusUpdateRequestDto;
import com.ewm.ewmmainservice.request.dto.model.RequestStatusUpdateResultDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PrivateRequestCreatorService {
    List<RequestDto> getRequestsByCreator(Long userId, Long eventId);

    RequestStatusUpdateResultDto updateRequests(Long userId, Long eventId, RequestStatusUpdateRequestDto requestStatusUpdateRequestDto);
}
