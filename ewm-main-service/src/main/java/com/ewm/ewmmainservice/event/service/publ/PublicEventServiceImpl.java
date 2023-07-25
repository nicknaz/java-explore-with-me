package com.ewm.ewmmainservice.event.service.publ;

import com.ewm.ewmmainservice.category.repository.CategoryRepositoryJPA;
import com.ewm.ewmmainservice.event.dto.EventFullDto;
import com.ewm.ewmmainservice.event.dto.mapper.EventMapper;
import com.ewm.ewmmainservice.event.model.Event;
import com.ewm.ewmmainservice.event.model.EventState;
import com.ewm.ewmmainservice.event.model.SortType;
import com.ewm.ewmmainservice.event.repository.EventRepositoryJPA;
import com.ewm.ewmmainservice.event.repository.LocationRepositoryJPA;
import com.ewm.ewmmainservice.exception.BadRequestException;
import com.ewm.ewmmainservice.exception.NotFoundedException;
import com.ewm.ewmmainservice.user.repository.UserRepositoryJPA;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import model.StatsHitDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.StatsClient;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
//@RequiredArgsConstructor
public class PublicEventServiceImpl implements PublicEventService {
    private EventRepositoryJPA eventRepositoryJPA;
    private UserRepositoryJPA userRepositoryJPA;
    private CategoryRepositoryJPA categoryRepositoryJPA;
    private LocationRepositoryJPA locationRepositoryJPA;
    //private StatsClient statsClient;

    @Autowired
    public PublicEventServiceImpl(EventRepositoryJPA eventRepositoryJPA,
                                 UserRepositoryJPA userRepositoryJPA,
                                 CategoryRepositoryJPA categoryRepositoryJPA,
                                 LocationRepositoryJPA locationRepositoryJPA) {
        this.eventRepositoryJPA = eventRepositoryJPA;
        this.userRepositoryJPA = userRepositoryJPA;
        this.categoryRepositoryJPA = categoryRepositoryJPA;
        this.locationRepositoryJPA = locationRepositoryJPA;
    }

    @Override
    public List<EventFullDto> getEventsList(String text, List<Long> categories, Boolean paid, String rangeStart,
                                            String rangeEnd, Boolean onlyAvailable, SortType sort, Pageable page,
                                            HttpServletRequest request) {
        if (categories != null && categoryRepositoryJPA.findAllById(categories).size() == 0) {
            throw new BadRequestException("Категории не найдены!");
        }
        log.info(categoryRepositoryJPA.findAllById(categories).toString());
        List<EventFullDto> result = eventRepositoryJPA.findByUserSearch(
                text != null ? "%" + text + "%" : null,
                categoryRepositoryJPA.findAllById(categories),
                paid,
                rangeStart != null ? LocalDateTime.parse(rangeStart, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : null,
                rangeEnd != null ? LocalDateTime.parse(rangeEnd, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : null,
                onlyAvailable != null ? onlyAvailable : null,
                (sort != null && sort.equals(SortType.VIEWS)) ? true : false,
                page).stream()
                .map(EventMapper::toEventFullDto)
                .collect(Collectors.toList());

        /*statsClient.create(StatsHitDto.builder()
                .ip(request.getRemoteAddr())
                .uri(request.getRequestURI())
                .app("ewm-main-service")
                .timestamp(LocalDateTime.now())
                .build());*/
        return result;
    }

    @Override
    public EventFullDto getEvent(Long id, HttpServletRequest request) {
        Event event = eventRepositoryJPA.findById(id)
                .orElseThrow(() -> new NotFoundedException("Событие не найдено"));
        EventFullDto eventFullDto = EventMapper.toEventFullDto(event);
        if (eventFullDto.getState() != EventState.PUBLISHED) {
            throw new NotFoundedException("Событие не найдено");
        }

        event.setViews(event.getViews() + 1);
        eventRepositoryJPA.save(event);

        /*statsClient.create(StatsHitDto.builder()
                .ip(request.getRemoteAddr())
                .uri(request.getRequestURI())
                .app("ewm-main-service")
                .timestamp(LocalDateTime.now())
                .build());*/
        return eventFullDto;
    }
}
