package ru.practicum.visit.model;

import lombok.*;
import ru.practicum.visit.annotation.Ip;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "stats")
public class StatsHit {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private String app;

    @Column
    private String uri;

    @Column
    @Ip
    private String ip;

    @Column(name = "hit_time")
    private LocalDateTime timestamp;
}
