package com.ewm.ewmmainservice.event.service.admin;

import com.ewm.ewmmainservice.category.model.Category;
import com.ewm.ewmmainservice.category.repository.CategoryRepositoryJPA;
import com.ewm.ewmmainservice.event.dto.EventFullDto;
import com.ewm.ewmmainservice.event.dto.UpdateEventAdminRequestDto;
import com.ewm.ewmmainservice.event.dto.mapper.EventMapper;
import com.ewm.ewmmainservice.event.model.Event;
import com.ewm.ewmmainservice.event.model.EventState;
import com.ewm.ewmmainservice.event.model.NewEventStateAdmin;
import com.ewm.ewmmainservice.event.repository.EventRepositoryJPA;
import com.ewm.ewmmainservice.event.repository.LocationRepositoryJPA;
import com.ewm.ewmmainservice.exception.BadRequestException;
import com.ewm.ewmmainservice.exception.ConflictException;
import com.ewm.ewmmainservice.exception.NotFoundedException;
import com.ewm.ewmmainservice.user.repository.UserRepositoryJPA;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional(readOnly = true)
public class AdminEventServiceImpl implements AdminEventService {

    private EventRepositoryJPA eventRepositoryJPA;
    private UserRepositoryJPA userRepositoryJPA;
    private CategoryRepositoryJPA categoryRepositoryJPA;
    private LocationRepositoryJPA locationRepositoryJPA;

    @Autowired
    public AdminEventServiceImpl(EventRepositoryJPA eventRepositoryJPA,
                                   UserRepositoryJPA userRepositoryJPA,
                                   CategoryRepositoryJPA categoryRepositoryJPA,
                                   LocationRepositoryJPA locationRepositoryJPA) {
        this.eventRepositoryJPA = eventRepositoryJPA;
        this.userRepositoryJPA = userRepositoryJPA;
        this.categoryRepositoryJPA = categoryRepositoryJPA;
        this.locationRepositoryJPA = locationRepositoryJPA;
    }

    @Override
    public List<EventFullDto> getEventsList(List<Long> users, EventState states,
                                            List<Long> categories, String rangeStart,
                                            String rangeEnd, Pageable page) {
        return eventRepositoryJPA.findByAdmin(
                users != null ? userRepositoryJPA.findAllById(users) : null,
                states,
                categories != null ? categoryRepositoryJPA.findAllById(categories) : null,
                rangeStart != null ? LocalDateTime.parse(rangeStart, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : null,
                rangeEnd != null ? LocalDateTime.parse(rangeEnd, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : null,
                page).stream()
                .map(EventMapper::toEventFullDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EventFullDto patchEvent(Long eventId, UpdateEventAdminRequestDto updateEventAdminRequestDto) {
        log.info("Обновление события админом");

        Event event = eventRepositoryJPA.findById(eventId)
                .orElseThrow(() -> new NotFoundedException("Событие не найдено"));

        if (updateEventAdminRequestDto == null) {
            return EventMapper.toEventFullDto(event);
        }

        if (updateEventAdminRequestDto.getAnnotation() != null) {
            event.setAnnotation(updateEventAdminRequestDto.getAnnotation());
        }

        if (updateEventAdminRequestDto.getCategory() != null) {
            Category category = categoryRepositoryJPA.findById(updateEventAdminRequestDto.getCategory())
                    .orElseThrow(() -> new NotFoundedException("Категория не найдена"));
            event.setCategory(category);
        }

        if (updateEventAdminRequestDto.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEventAdminRequestDto.getParticipantLimit());
        }

        if (updateEventAdminRequestDto.getRequestModeration() != null) {
            event.setRequestModeration(updateEventAdminRequestDto.getRequestModeration());
        }

        if (updateEventAdminRequestDto.getDescription() != null) {
            event.setDescription(updateEventAdminRequestDto.getDescription());
        }

        if (updateEventAdminRequestDto.getLocation() != null) {
            locationRepositoryJPA.save(updateEventAdminRequestDto.getLocation());
            event.setLocation(updateEventAdminRequestDto.getLocation());
        }

        if (updateEventAdminRequestDto.getPaid() != null) {
            event.setPaid(updateEventAdminRequestDto.getPaid());
        }

        if (updateEventAdminRequestDto.getTitle() != null) {
            event.setTitle(updateEventAdminRequestDto.getTitle());
        }

        if (updateEventAdminRequestDto.getStateAction() != null) {
            NewEventStateAdmin stateAction = updateEventAdminRequestDto.getStateAction();
            if (event.getState().equals(EventState.CANCELED)) {
                throw new ConflictException("Событие уже отменено");
            }
            if (event.getState().equals(EventState.PUBLISHED)) {
                throw new ConflictException("Событие уже опубликовано");
            }
            switch (stateAction) {
                case PUBLISH_EVENT:
                    event.setState(EventState.PUBLISHED);
                    event.setPublishedOn(LocalDateTime.now());
                    break;
                case REJECT_EVENT:
                    event.setState(EventState.CANCELED);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + stateAction);
            }
        }

        if (updateEventAdminRequestDto.getEventDate() != null) {
            LocalDateTime eventDateTime = updateEventAdminRequestDto.getEventDate();
            LocalDateTime currentDateTime = LocalDateTime.now();
            LocalDateTime publishedDateTime = event.getPublishedOn();

            if (eventDateTime.isBefore(currentDateTime)
                    || (publishedDateTime != null && eventDateTime.isBefore(publishedDateTime))) {
                throw new BadRequestException("Неккоректные даты");
            }

            event.setEventDate(updateEventAdminRequestDto.getEventDate());
        }

        log.info("Все поля обновлены");

        return EventMapper.toEventFullDto(eventRepositoryJPA.save(event));
    }
}
