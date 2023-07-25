package com.ewm.ewmmainservice.event.service.priv;

import com.ewm.ewmmainservice.category.model.Category;
import com.ewm.ewmmainservice.category.repository.CategoryRepositoryJPA;
import com.ewm.ewmmainservice.event.dto.EventFullDto;
import com.ewm.ewmmainservice.event.dto.EventRequestCreateDto;
import com.ewm.ewmmainservice.event.dto.EventShortDto;
import com.ewm.ewmmainservice.event.dto.UpdateEventUserRequestDto;
import com.ewm.ewmmainservice.event.dto.mapper.EventMapper;
import com.ewm.ewmmainservice.event.model.Event;
import com.ewm.ewmmainservice.event.model.EventState;
import com.ewm.ewmmainservice.event.model.Location;
import com.ewm.ewmmainservice.event.model.NewEventStateInitiator;
import com.ewm.ewmmainservice.event.repository.EventRepositoryJPA;
import com.ewm.ewmmainservice.event.repository.LocationRepositoryJPA;
import com.ewm.ewmmainservice.exception.ConflictException;
import com.ewm.ewmmainservice.exception.NotFoundedException;
import com.ewm.ewmmainservice.user.model.User;
import com.ewm.ewmmainservice.user.repository.UserRepositoryJPA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PrivateEventServiceImpl implements PrivateEventService {
    private EventRepositoryJPA eventRepositoryJPA;
    private UserRepositoryJPA userRepositoryJPA;
    private CategoryRepositoryJPA categoryRepositoryJPA;
    private LocationRepositoryJPA locationRepositoryJPA;

    @Autowired
    public PrivateEventServiceImpl(EventRepositoryJPA eventRepositoryJPA,
                                   UserRepositoryJPA userRepositoryJPA,
                                   CategoryRepositoryJPA categoryRepositoryJPA,
                                   LocationRepositoryJPA locationRepositoryJPA) {
        this.eventRepositoryJPA = eventRepositoryJPA;
        this.userRepositoryJPA = userRepositoryJPA;
        this.categoryRepositoryJPA = categoryRepositoryJPA;
        this.locationRepositoryJPA = locationRepositoryJPA;
    }


    @Override
    public EventFullDto create(Long userId, EventRequestCreateDto eventRequestCreateDto) {
        Category category = categoryRepositoryJPA.findById(eventRequestCreateDto.getCategory())
                .orElseThrow(() -> new NotFoundedException("Категория не найден"));
        User initiator = userRepositoryJPA.findById(userId)
                .orElseThrow(() -> new NotFoundedException("Пользователь не найден"));
        Location location = locationRepositoryJPA.save(eventRequestCreateDto.getLocation());
        return EventMapper.toEventFullDto(eventRepositoryJPA.save(EventMapper.toEventFromNewEventDto(eventRequestCreateDto,
                location, category, initiator)));
    }

    @Override
    public List<EventShortDto> getEventsListByUser(Long userId, Pageable page) {
        User initiator = userRepositoryJPA.findById(userId)
                .orElseThrow(() -> new NotFoundedException("Пользователь не найден"));
        return eventRepositoryJPA.findAllByInitiator(initiator, page)
                .stream()
                .map(EventMapper::toEventShortDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDto getEventByUser(Long userId, Long eventId) {
        Event event = eventRepositoryJPA.findById(eventId)
                .orElseThrow(() -> new NotFoundedException("Событие не найдено"));

        if (event.getInitiator().getId() != userId) {
            throw new NotFoundedException("Событие не найдено");
        }

        User initiator = userRepositoryJPA.findById(userId)
                .orElseThrow(() -> new NotFoundedException("Пользователь не найден"));


        return EventMapper.toEventFullDto(event);
    }

    @Override
    public EventFullDto patchEvent(Long userId, Long eventId, UpdateEventUserRequestDto updateEventUserRequestDto) {
        Event event = eventRepositoryJPA.findById(eventId)
                .orElseThrow(() -> new NotFoundedException("Событие не найдено"));

        if (event.getInitiator().getId() != userId) {
            throw new NotFoundedException("Событие не найдено");
        }

        if (event.getPublishedOn() != null) {
            throw new ConflictException("Событие уже опубликовано");
        }

        if (updateEventUserRequestDto == null) {
            return EventMapper.toEventFullDto(event);
        }

        if (updateEventUserRequestDto.getAnnotation() != null) {
            event.setAnnotation(updateEventUserRequestDto.getAnnotation());
        }

        if (updateEventUserRequestDto.getCategory() != null) {
            event.setCategory(categoryRepositoryJPA.findById(updateEventUserRequestDto.getCategory())
                    .orElseThrow(() -> new NotFoundedException("Категория не найдена")));
        }

        if (updateEventUserRequestDto.getDescription() != null) {
            event.setDescription(updateEventUserRequestDto.getDescription());
        }

        if (updateEventUserRequestDto.getEventDate() != null) {
            LocalDateTime eventDateTime = updateEventUserRequestDto.getEventDate();
            event.setEventDate(updateEventUserRequestDto.getEventDate());
        }

        if (updateEventUserRequestDto.getLocation() != null) {
            event.setLocation(updateEventUserRequestDto.getLocation());
        }

        if (updateEventUserRequestDto.getRequestModeration() != null) {
            event.setRequestModeration(updateEventUserRequestDto.getRequestModeration());
        }

        if (updateEventUserRequestDto.getTitle() != null) {
            event.setTitle(updateEventUserRequestDto.getTitle());
        }

        if (updateEventUserRequestDto.getPaid() != null) {
            event.setPaid(updateEventUserRequestDto.getPaid());
        }

        if (updateEventUserRequestDto.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEventUserRequestDto.getParticipantLimit());
        }

        if (updateEventUserRequestDto.getStateAction() != null) {
            event.setState(updateEventUserRequestDto.getStateAction().equals(NewEventStateInitiator.SEND_TO_REVIEW)
                    ? EventState.PENDING
                    : EventState.CANCELED);
        }

        return EventMapper.toEventFullDto(eventRepositoryJPA.save(event));

    }
}
