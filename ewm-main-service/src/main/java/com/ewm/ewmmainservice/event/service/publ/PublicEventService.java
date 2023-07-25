package com.ewm.ewmmainservice.event.service.publ;

import com.ewm.ewmmainservice.event.dto.EventFullDto;
import com.ewm.ewmmainservice.event.model.SortType;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public interface PublicEventService {

    List<EventFullDto> getEventsList(String text, List<Long> categories, Boolean paid, String rangeStart,
                                     String rangeEnd, Boolean onlyAvailable, SortType sort, Pageable page,
                                     HttpServletRequest request);

    EventFullDto getEvent(Long id, HttpServletRequest request);
}
