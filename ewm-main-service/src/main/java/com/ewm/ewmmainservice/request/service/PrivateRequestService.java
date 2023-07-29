package com.ewm.ewmmainservice.request.service;

import com.ewm.ewmmainservice.request.dto.model.RequestDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PrivateRequestService {

    RequestDto create(Long userId, Long eventId);

    List<RequestDto> getRequestsListByUser(Long userId);

    RequestDto cancel(Long userId, Long requestId);

}
