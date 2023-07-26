package ru.practicum;

import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StatsResponseDto {
    private String app;

    private String uri;

    private long hits;
}