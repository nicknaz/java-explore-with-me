package com.ewm.ewmmainservice.event.service.publ;

import com.ewm.ewmmainservice.event.dto.EventFullDto;
import com.ewm.ewmmainservice.event.model.SearchEventParams;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public interface PublicEventService {

    List<EventFullDto> getEventsList(SearchEventParams searchEventParams);

    EventFullDto getEvent(Long id, HttpServletRequest request);
}
