package ru.practicum.visit.mapper;

import ru.practicum.visit.model.StatsHit;
import ru.practicum.visit.model.StatsResponse;
import ru.practicum.StatsHitDto;
import ru.practicum.StatsResponseDto;

public class StatsMapper {
    public static StatsHitDto toStatsHitDto(StatsHit hit) {
        return StatsHitDto.builder()
                .id(hit.getId())
                .app(hit.getApp())
                .uri(hit.getUri())
                .ip(hit.getIp())
                .timestamp(hit.getTimestamp())
                .build();
    }

    public static StatsHit toStatsHit(StatsHitDto dto) {
        return StatsHit.builder()
                .id(dto.getId())
                .app(dto.getApp())
                .uri(dto.getUri())
                .ip(dto.getIp())
                .timestamp(dto.getTimestamp())
                .build();
    }

    public static StatsResponseDto toStatsResponseDto(StatsResponse response) {
        return StatsResponseDto.builder()
                .app(response.getApp())
                .uri(response.getUri())
                .hits(response.getHits())
                .build();
    }
}
