package com.ewm.ewmmainservice.event.repository;

import com.ewm.ewmmainservice.category.model.Category;
import com.ewm.ewmmainservice.event.model.Event;
import com.ewm.ewmmainservice.event.model.EventState;
import com.ewm.ewmmainservice.user.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepositoryJPA extends JpaRepository<Event, Long> {
    List<Event> findAllByInitiator(User user, Pageable page);

    @Query(value = "select ev from Event as ev " +
            "where (cast(:users as int) is null or ev.initiator in :users) " +
            "and (cast(:states as text) is null or ev.state = :states) " +
            "and (cast(:categories as int) is null or ev.category in :categories) " +
            "and (cast(:rangeStart as timestamp) is null or ev.eventDate >= :rangeStart) " +
            "and (cast(:rangeEnd as timestamp) is null or ev.eventDate <= :rangeEnd) " +
            "order by ev.id desc")
    List<Event> findByAdmin(@Param("users") List<User> users, @Param("states") EventState states,
                            @Param("categories") List<Category> categories, @Param("rangeStart") LocalDateTime rangeStart,
                            @Param("rangeEnd") LocalDateTime rangeEnd, Pageable page);

    @Query(value = "select ev from Event as ev " +
            "where (cast(:text as text) is null or lower(ev.annotation) like :text or lower(ev.description) like :text) " +
            "and (cast(:categories as integer) is null or ev.category in :categories) " +
            "or (cast(:paid as boolean) is null or ev.paid = :paid) " +
            "and (cast(:rangeStart as timestamp) is null or ev.eventDate >= :rangeStart) " +
            "and (cast(:rangeEnd as timestamp) is null or ev.eventDate <= :rangeEnd) " +
            "and ((ev.confirmedRequest < ev.participantLimit and :onlyAvailable = true) or :onlyAvailable = false) " +
            "order by (case when :sortViews = true then cast(ev.views as text) else cast(ev.eventDate as text) end) desc")
    List<Event> findByUserSearch(@Param("text") String text, @Param("categories") List<Category> categories,
                                 @Param("paid") Boolean paid, @Param("rangeStart") LocalDateTime rangeStart,
                                 @Param("rangeEnd") LocalDateTime rangeEnd, @Param("onlyAvailable") Boolean onlyAvailable,
                                 @Param("sortViews") Boolean sortViews, Pageable page);

    List<Event> findEventsByCategory(Category category);
}
