package com.ewm.ewmmainservice.event.model;

import com.ewm.ewmmainservice.category.model.Category;
import com.ewm.ewmmainservice.user.model.User;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "events")
public class Event {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(length = 2000)
    private String annotation;

    @ManyToOne
    @JoinColumn(name = "category", referencedColumnName = "id")
    private Category category;

    @Column(name = "confirmed_request")
    private Integer confirmedRequest;

    @Column(name = "event_date")
    private LocalDateTime eventDate;

    @ManyToOne
    @JoinColumn(name = "initiator", referencedColumnName = "id")
    private User initiator;

    @Column
    private Boolean paid;

    @Column
    private String title;

    @Column
    private Long views;

    @Column(length = 7000)
    private String description;

    @Column(name = "created_on")
    private LocalDateTime createdOn;

    @ManyToOne
    @JoinColumn(name = "location", referencedColumnName = "id")
    private Location location;

    @Column
    private Long participantLimit;

    @Column(name = "published_on")
    private LocalDateTime publishedOn;

    @Column
    private Boolean requestModeration;

    @Enumerated(EnumType.STRING)
    @Column
    private EventState state;

}
