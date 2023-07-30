package com.ewm.ewmmainservice.event.service.publ;

import com.ewm.ewmmainservice.category.repository.CategoryRepositoryJPA;
import com.ewm.ewmmainservice.event.dto.EventFullDto;
import com.ewm.ewmmainservice.event.dto.mapper.EventMapper;
import com.ewm.ewmmainservice.event.model.Event;
import com.ewm.ewmmainservice.event.model.EventState;
import com.ewm.ewmmainservice.event.model.SearchEventParams;
import com.ewm.ewmmainservice.event.model.SortType;
import com.ewm.ewmmainservice.event.repository.EventRepositoryJPA;
import com.ewm.ewmmainservice.event.repository.LocationRepositoryJPA;
import com.ewm.ewmmainservice.exception.BadRequestException;
import com.ewm.ewmmainservice.exception.NotFoundedException;
import com.ewm.ewmmainservice.user.model.User;
import com.ewm.ewmmainservice.user.repository.UserRepositoryJPA;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.StatsClient;
import ru.practicum.StatsHitDto;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional(readOnly = true)
public class PublicEventServiceImpl implements PublicEventService {
    private EventRepositoryJPA eventRepositoryJPA;
    private UserRepositoryJPA userRepositoryJPA;
    private CategoryRepositoryJPA categoryRepositoryJPA;
    private LocationRepositoryJPA locationRepositoryJPA;
    private StatsClient statsClient;


    @Autowired
    public PublicEventServiceImpl(EventRepositoryJPA eventRepositoryJPA,
                                 UserRepositoryJPA userRepositoryJPA,
                                 CategoryRepositoryJPA categoryRepositoryJPA,
                                 LocationRepositoryJPA locationRepositoryJPA,
                                 StatsClient statsClient) {
        this.eventRepositoryJPA = eventRepositoryJPA;
        this.userRepositoryJPA = userRepositoryJPA;
        this.categoryRepositoryJPA = categoryRepositoryJPA;
        this.locationRepositoryJPA = locationRepositoryJPA;
        this.statsClient = statsClient;

    }

    @Override
    @Transactional(readOnly = true)
    public List<EventFullDto> getEventsList(SearchEventParams searchEventParams) {
        if (searchEventParams.getCategories() != null && categoryRepositoryJPA.findAllById(searchEventParams.getCategories()).size() == 0) {
            throw new BadRequestException("Категории не найдены!");
        }
        log.info("check1");
        List<EventFullDto> result = eventRepositoryJPA.findByUserSearch(
                searchEventParams.getText() != null ? "%" + searchEventParams.getText() + "%" : null,
                searchEventParams.getCategories() != null ? categoryRepositoryJPA.findAllById(searchEventParams.getCategories()) : null,
                searchEventParams.getPaid(),
                searchEventParams.getRangeStart() != null ? LocalDateTime.parse(searchEventParams.getRangeStart(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : null,
                searchEventParams.getRangeEnd() != null ? LocalDateTime.parse(searchEventParams.getRangeEnd(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : null,
                searchEventParams.getOnlyAvailable() != null ? searchEventParams.getOnlyAvailable() : false,
                (searchEventParams.getSort() != null && searchEventParams.getSort().equals(SortType.VIEWS)) ? true : false,
                searchEventParams.getPage()).stream()
                .map(EventMapper::toEventFullDto)
                .collect(Collectors.toList());


        statsClient.create(StatsHitDto.builder()
                .ip(searchEventParams.getRequest().getRemoteAddr())
                .uri(searchEventParams.getRequest().getRequestURI())
                .app("ewm-main-service")
                .timestamp(LocalDateTime.now())
                .build());
        return result;
    }

    @Transactional(readOnly = true)
    @Override
    public EventFullDto getEvent(Long id, HttpServletRequest request) {
        Event event = eventRepositoryJPA.findById(id)
                .orElseThrow(() -> new NotFoundedException("Событие не найдено"));

        if (event.getState() != EventState.PUBLISHED) {
            throw new NotFoundedException("Событие не найдено");
        }


        statsClient.create(StatsHitDto.builder()
                .ip(request.getRemoteAddr())
                .uri(request.getRequestURI())
                .app("ewm-main-service")
                .timestamp(LocalDateTime.now())
                .build());

        event.setViews(event.getViews() + 1);
        eventRepositoryJPA.save(event);

        EventFullDto eventFullDto = EventMapper.toEventFullDto(event);
        return eventFullDto;
    }

    @Override
    public List<EventFullDto> getFeeds(Long userId, Pageable page, HttpServletRequest request) {
        List<User> tracked = userRepositoryJPA.findById(userId)
                .orElseThrow(() -> new NotFoundedException("Пользователь не найдено"))
                .getTracked();

        if (tracked == null || tracked.isEmpty()) {
            throw new BadRequestException("Вы ни на кого ещё не подписаны!");
        }

        List<EventFullDto> result = eventRepositoryJPA.findByFeeds(tracked, page).stream()
                .map(EventMapper::toEventFullDto)
                .collect(Collectors.toList());


        statsClient.create(StatsHitDto.builder()
                .ip(request.getRemoteAddr())
                .uri(request.getRequestURI())
                .app("ewm-main-service")
                .timestamp(LocalDateTime.now())
                .build());
        return result;
    }
}
