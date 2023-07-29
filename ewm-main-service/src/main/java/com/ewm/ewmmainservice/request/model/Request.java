package com.ewm.ewmmainservice.request.model;

import com.ewm.ewmmainservice.event.model.Event;
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
@Table(name = "requests")
public class Request {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private LocalDateTime created;

    @ManyToOne
    @JoinColumn(name = "event", referencedColumnName = "id")
    private Event event;

    @ManyToOne
    @JoinColumn(name = "requester", referencedColumnName = "id")
    private User requester;

    @Enumerated(EnumType.STRING)
    @Column
    private RequestStatus status;
}
