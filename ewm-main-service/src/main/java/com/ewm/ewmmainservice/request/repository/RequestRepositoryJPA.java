package com.ewm.ewmmainservice.request.repository;

import com.ewm.ewmmainservice.event.model.Event;
import com.ewm.ewmmainservice.request.model.Request;
import com.ewm.ewmmainservice.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequestRepositoryJPA extends JpaRepository<Request, Long> {
    List<Request> findAllByEvent(Event event);

    List<Request> findAllByRequester(User user);

    Request findRequestByRequesterAndEvent(User user, Event event);

    List<Request> findAllByEventAndEventInitiator(Event event, User user);
}
