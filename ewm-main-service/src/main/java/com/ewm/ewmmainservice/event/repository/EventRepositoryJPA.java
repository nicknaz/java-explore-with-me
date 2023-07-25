package com.ewm.ewmmainservice.event.repository;

import com.ewm.ewmmainservice.category.model.Category;
import com.ewm.ewmmainservice.event.model.Event;
import com.ewm.ewmmainservice.event.model.EventState;
import com.ewm.ewmmainservice.user.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepositoryJPA extends JpaRepository<Event, Long> {
    List<Event> findAllByInitiator(User user, Pageable page);

    @Modifying
    @Query(value = "select ev from Event as ev " +
            "where ((?1) is null or ev.initiator in (?1)) " +
            "and ((?2) is null or ev.state = (?2)) " +
            "and ((?3) is null or ev.category in (?3)) " +
            "and ((?4) is null or ev.eventDate >= (?4)) " +
            "and ((?5) is null or ev.eventDate <= (?5)) " +
            "order by ev.id desc", nativeQuery = true)
    List<Event> findByAdmin(List<User> users, EventState states,
                            List<Category> categories, LocalDateTime rangeStart,
                            LocalDateTime rangeEnd, Pageable page);

    @Modifying
    @Query(value = "select ev from Event as ev " +
            "where (:text is null or lower(ev.annotation) like :text or lower(ev.description) like :text) " +
            "and (:categories is null or ev.category in :categories) " +
            "or (:paid is null or ev.paid = :paid) " +
            "and (:rangeStart is null or ev.eventDate >= :rangeStart) " +
            "and (:rangeEnd is null or ev.eventDate <= :rangeEnd) " +
            "and (:onlyAvailable is null or (ev.confirmedRequest < ev.participantLimit and :onlyAvailable = true) or :onlyAvailable = false) " +
            "order by (case when :sortViews = true then cast(ev.views as text) else cast(ev.eventDate as text) end) desc", nativeQuery = true)
    List<Event> findByUserSearch(@Param("text") String text, @Param("categories") List<Category> categories,
                                 @Param("paid") Boolean paid, @Param("rangeStart") LocalDateTime rangeStart,
                                 @Param("rangeEnd") LocalDateTime rangeEnd, @Param("onlyAvailable") Boolean onlyAvailable,
                                 @Param("sortViews") Boolean sortViews, Pageable page);

    List<Event> findEventsByCategory(Category category);
}
