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
            "where (ev.initiator in (?1) or (?1) is null) " +
            "and (ev.state = (?2) or (?2) is null) " +
            "and (ev.category in (?3) or (?3) is null) " +
            "and (ev.eventDate >= (?4) or (?4) is null) " +
            "and (ev.eventDate <= (?5) or (?5) is null) " +
            "order by ev.id desc")
    List<Event> findByAdmin(List<User> users, EventState states,
                            List<Category> categories, LocalDateTime rangeStart,
                            LocalDateTime rangeEnd, Pageable page);

    @Query("select ev from Event as ev " +
            "where (lower(ev.annotation) in :text or lower(ev.description) in :text or :text is null) " +
            "and (ev.category in :categories or :categories is null) " +
            "or (ev.paid = :paid or :paid is null) " +
            "and (ev.eventDate >= :rangeStart or :rangeStart is null) " +
            "and (ev.eventDate <= :rangeEnd or :rangeEnd is null) " +
            "and ((ev.confirmedRequest < ev.participantLimit and :onlyAvailable = true) or :onlyAvailable = false or :onlyAvailable is null) " +
            "order by (case when :sortViews = true then cast(ev.views as text) else cast(ev.eventDate as text) end) desc")
    List<Event> findByUserSearch(@Param("text") String text, @Param("categories") List<Category> categories,
                                 @Param("paid") Boolean paid, @Param("rangeStart") LocalDateTime rangeStart,
                                 @Param("rangeEnd") LocalDateTime rangeEnd, @Param("onlyAvailable") Boolean onlyAvailable,
                                 @Param("sortViews") Boolean sortViews, Pageable page);

    List<Event> findEventsByCategory(Category category);
}
