package com.ewm.ewmmainservice.request.service;

import com.ewm.ewmmainservice.category.repository.CategoryRepositoryJPA;
import com.ewm.ewmmainservice.event.model.Event;
import com.ewm.ewmmainservice.event.model.EventState;
import com.ewm.ewmmainservice.event.repository.EventRepositoryJPA;
import com.ewm.ewmmainservice.event.repository.LocationRepositoryJPA;
import com.ewm.ewmmainservice.exception.ConflictException;
import com.ewm.ewmmainservice.exception.NotFoundedException;
import com.ewm.ewmmainservice.request.dto.mapper.RequestMapper;
import com.ewm.ewmmainservice.request.dto.model.RequestDto;
import com.ewm.ewmmainservice.request.model.Request;
import com.ewm.ewmmainservice.request.model.RequestStatus;
import com.ewm.ewmmainservice.request.repository.RequestRepositoryJPA;
import com.ewm.ewmmainservice.user.model.User;
import com.ewm.ewmmainservice.user.repository.UserRepositoryJPA;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
public class PrivateRequestServiceImpl implements PrivateRequestService {
    private EventRepositoryJPA eventRepositoryJPA;
    private UserRepositoryJPA userRepositoryJPA;
    private CategoryRepositoryJPA categoryRepositoryJPA;
    private LocationRepositoryJPA locationRepositoryJPA;
    private RequestRepositoryJPA requestRepositoryJPA;

    @Autowired
    public PrivateRequestServiceImpl(EventRepositoryJPA eventRepositoryJPA,
                                   UserRepositoryJPA userRepositoryJPA,
                                   CategoryRepositoryJPA categoryRepositoryJPA,
                                   LocationRepositoryJPA locationRepositoryJPA,
                                     RequestRepositoryJPA requestRepositoryJPA) {
        this.eventRepositoryJPA = eventRepositoryJPA;
        this.userRepositoryJPA = userRepositoryJPA;
        this.categoryRepositoryJPA = categoryRepositoryJPA;
        this.locationRepositoryJPA = locationRepositoryJPA;
        this.requestRepositoryJPA = requestRepositoryJPA;
    }


    @Override
    @Transactional
    public RequestDto create(Long userId, Long eventId) {
        log.info("Создание запроса с userId = {} и eventId = {}", userId, eventId);

        User user = userRepositoryJPA.findById(userId)
                .orElseThrow(() -> new NotFoundedException("Пользователь не найден"));

        log.info("Успешное получение юзера при создании запроса");

        Event event = eventRepositoryJPA.findById(eventId)
                .orElseThrow(() -> new NotFoundedException("Событие не найдено"));

        log.info("Успешное получение события с id = {} при создании запроса", event.getId());

        if (event.getInitiator().getId() == userId) {
            log.info("Нельзя оставить заявку на своё событие!");
            throw new ConflictException("Нельзя оставить заявку на своё событие!");
        }

        if (requestRepositoryJPA.findRequestByRequesterAndEvent(user, event) != null) {
            throw new ConflictException("Заявка уже подана");
        }

        if (event.getState() != EventState.PUBLISHED) {
            throw new ConflictException("Событие ещё не опубликовано");
        }

        if (!event.getRequestModeration() && event.getParticipantLimit() != 0
            && requestRepositoryJPA.findAllByEvent(event).size() >= event.getParticipantLimit()) {
            log.info("Превышен лимит заявок, доступно {}, отправлено {}",
                    event.getParticipantLimit(),
                    requestRepositoryJPA.findAllByEvent(event).size());
            throw new ConflictException("Превышен лимит заявок");
        }

        log.info("Успешная валидация запроса");

        Request request = Request
                .builder()
                .created(LocalDateTime.now())
                .requester(user)
                .event(event)
                .status(event.getParticipantLimit() == 0 ? RequestStatus.CONFIRMED : RequestStatus.PENDING)
                .build();

        request = requestRepositoryJPA.save(request);

        log.info("Успешное создание запроса");

        return RequestMapper.toRequestDto(request);
    }

    @Override
    public List<RequestDto> getRequestsListByUser(Long userId) {
        User user = userRepositoryJPA.findById(userId)
                .orElseThrow(() -> new NotFoundedException("Пользователь не найден"));

        return requestRepositoryJPA.findAllByRequester(user)
                .stream()
                .map(RequestMapper::toRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public RequestDto cancel(Long userId, Long requestId) {
        Request request = requestRepositoryJPA.findById(requestId)
                .orElseThrow(() -> new NotFoundedException("Событие не найдено"));

        if (request.getRequester().getId() != userId) {
            throw new NotFoundedException("Событие не найдено");
        }

        User user = userRepositoryJPA.findById(userId)
                .orElseThrow(() -> new NotFoundedException("Пользователь не найден"));

        request.setStatus(RequestStatus.CANCELED);

        return RequestMapper.toRequestDto(requestRepositoryJPA.save(request));
    }

}
