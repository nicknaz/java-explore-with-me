package model;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StatsHitDto {
    private long id;

    private String app;

    private String uri;

    private String ip;

    private LocalDateTime timestamp;
}
