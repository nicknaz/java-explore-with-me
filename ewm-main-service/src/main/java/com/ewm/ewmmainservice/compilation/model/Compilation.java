package com.ewm.ewmmainservice.compilation.model;

import com.ewm.ewmmainservice.event.model.Event;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "compilations", uniqueConstraints = @UniqueConstraint(columnNames = "title"))
public class Compilation {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Boolean pinned;

    @Column
    private String title;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "events_compilations",
            joinColumns = @JoinColumn(name = "compilation_id"),
            inverseJoinColumns = @JoinColumn(name = "event_id")
    )
    private List<Event> events;
}
