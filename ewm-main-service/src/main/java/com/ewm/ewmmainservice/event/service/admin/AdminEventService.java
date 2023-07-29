package com.ewm.ewmmainservice.event.service.admin;

import com.ewm.ewmmainservice.event.dto.EventFullDto;
import com.ewm.ewmmainservice.event.dto.UpdateEventAdminRequestDto;
import com.ewm.ewmmainservice.event.model.EventState;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AdminEventService {

    List<EventFullDto> getEventsList(List<Long> users,
                                     EventState states,
                                     List<Long> categoriesId,
                                     String rangeStart,
                                     String rangeEnd,
                                     Pageable page);

    EventFullDto patchEvent(Long eventId,
                            UpdateEventAdminRequestDto updateEventAdminRequestDto);
}
