package com.ewm.ewmmainservice.event.service.priv;

import com.ewm.ewmmainservice.event.dto.EventFullDto;
import com.ewm.ewmmainservice.event.dto.EventRequestCreateDto;
import com.ewm.ewmmainservice.event.dto.EventShortDto;
import com.ewm.ewmmainservice.event.dto.UpdateEventUserRequestDto;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PrivateEventService {

    EventFullDto create(Long userId,
                        EventRequestCreateDto eventRequestCreateDto);

    List<EventShortDto> getEventsListByUser(Long userId,
                                      Pageable page);

    EventFullDto getEventByUser(Long userId, Long eventId);

    EventFullDto patchEvent(Long userId, Long eventId, UpdateEventUserRequestDto updateEventUserRequestDto);
}
