package ru.practicum.visit.model;

import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StatsResponse {
    private String app;

    private String uri;

    private long hits;
}
