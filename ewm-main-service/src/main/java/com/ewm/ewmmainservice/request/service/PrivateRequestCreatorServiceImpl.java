package com.ewm.ewmmainservice.request.service;

import com.ewm.ewmmainservice.category.repository.CategoryRepositoryJPA;
import com.ewm.ewmmainservice.event.model.Event;
import com.ewm.ewmmainservice.event.repository.EventRepositoryJPA;
import com.ewm.ewmmainservice.event.repository.LocationRepositoryJPA;
import com.ewm.ewmmainservice.exception.ConflictException;
import com.ewm.ewmmainservice.exception.NotFoundedException;
import com.ewm.ewmmainservice.request.dto.mapper.RequestMapper;
import com.ewm.ewmmainservice.request.dto.model.RequestDto;
import com.ewm.ewmmainservice.request.dto.model.RequestStatusUpdateRequestDto;
import com.ewm.ewmmainservice.request.dto.model.RequestStatusUpdateResultDto;
import com.ewm.ewmmainservice.request.model.NewRequestStatusUpdate;
import com.ewm.ewmmainservice.request.model.Request;
import com.ewm.ewmmainservice.request.model.RequestStatus;
import com.ewm.ewmmainservice.request.repository.RequestRepositoryJPA;
import com.ewm.ewmmainservice.user.model.User;
import com.ewm.ewmmainservice.user.repository.UserRepositoryJPA;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PrivateRequestCreatorServiceImpl implements PrivateRequestCreatorService {
    private EventRepositoryJPA eventRepositoryJPA;
    private UserRepositoryJPA userRepositoryJPA;
    private CategoryRepositoryJPA categoryRepositoryJPA;
    private LocationRepositoryJPA locationRepositoryJPA;
    private RequestRepositoryJPA requestRepositoryJPA;

    @Autowired
    public PrivateRequestCreatorServiceImpl(EventRepositoryJPA eventRepositoryJPA,
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
    public List<RequestDto> getRequestsByCreator(Long userId, Long eventId) {
        User user = userRepositoryJPA.findById(userId)
                .orElseThrow(() -> new NotFoundedException("Пользователь не найден"));

        Event event = eventRepositoryJPA.findById(eventId)
                .orElseThrow(() -> new NotFoundedException("Событие не найдено"));

        return requestRepositoryJPA.findAllByEventAndEventInitiator(event, user)
                .stream()
                .map(RequestMapper::toRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public RequestStatusUpdateResultDto updateRequests(Long userId, Long eventId, RequestStatusUpdateRequestDto requestStatusUpdateRequestDto) {
        log.info("Начато обновление запросов создателем события");
        User user = userRepositoryJPA.findById(userId)
                .orElseThrow(() -> new NotFoundedException("Пользователь не найден"));

        Event event = eventRepositoryJPA.findById(eventId)
                .orElseThrow(() -> new NotFoundedException("Событие не найдено"));

        log.info("событие и пользователь найден");

        if (requestStatusUpdateRequestDto.getStatus() == NewRequestStatusUpdate.CONFIRMED
                && requestStatusUpdateRequestDto.getRequestIds().size() + event.getConfirmedRequest() > event.getParticipantLimit()) {
            log.info("Превышен лимит заявок");
            throw new ConflictException("Превышен лимит заявок");
        }

        List<Request> requests = requestRepositoryJPA.findAllByEventAndEventInitiator(event, user)
                .stream()
                .filter(x -> requestStatusUpdateRequestDto.getRequestIds().contains(x.getId()))
                .collect(Collectors.toList());

        if (requestStatusUpdateRequestDto.getStatus().equals(NewRequestStatusUpdate.REJECTED)) {
            if (requests.stream()
                    .anyMatch(r -> r.getStatus().equals(RequestStatus.CONFIRMED))) {
                throw new ConflictException("Уже есть подтвержденные заявки, которые нельзя отклонить");
            }
        }

        if (requestStatusUpdateRequestDto.getStatus().equals(NewRequestStatusUpdate.CONFIRMED)) {
            if (requests.stream()
                    .anyMatch(r -> r.getStatus().equals(RequestStatus.REJECTED))) {
                throw new ConflictException("Уже есть отклоненные заявки, которые нельзя подтвердить");
            }
        }

        for (Request request : requests) {
            request.setStatus(RequestStatus.valueOf(requestStatusUpdateRequestDto.getStatus().toString()));
            requestRepositoryJPA.save(request);
        }

        List<RequestDto> result = requests.stream()
                .map(RequestMapper::toRequestDto)
                .collect(Collectors.toList());

        log.info("Успешное обновление запросов создателем события");
        if (requestStatusUpdateRequestDto.getStatus().equals(NewRequestStatusUpdate.REJECTED)) {
            return RequestStatusUpdateResultDto.builder()
                    .rejectedRequests(result).build();
        } else {
            event.setConfirmedRequest((event.getConfirmedRequest() != null ? event.getConfirmedRequest() : 0) + result.size());
            eventRepositoryJPA.save(event);
            return RequestStatusUpdateResultDto.builder()
                    .confirmedRequests(result).build();
        }

    }
}
