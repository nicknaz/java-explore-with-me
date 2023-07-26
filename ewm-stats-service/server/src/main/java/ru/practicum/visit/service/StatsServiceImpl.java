package ru.practicum.visit.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.StatsHitDto;
import ru.practicum.StatsResponseDto;
import ru.practicum.visit.exception.BadRequestException;
import ru.practicum.visit.mapper.StatsMapper;
import ru.practicum.visit.model.StatsHit;
import ru.practicum.visit.repository.StatsRepositoryJPA;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional(readOnly = true)
public class StatsServiceImpl implements StatsService {
    private StatsRepositoryJPA statsRepositoryJPA;

    @Autowired
    public StatsServiceImpl(StatsRepositoryJPA statsRepositoryJPA) {
        this.statsRepositoryJPA = statsRepositoryJPA;
    }

    @Override
    @Transactional
    public void create(StatsHitDto dto) {
        StatsHit hitSaved = statsRepositoryJPA.save(StatsMapper.toStatsHit(dto));
        log.info("StatsHit created with id = {}", hitSaved.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public List<StatsResponseDto> getStats(LocalDateTime start,
                                           LocalDateTime end,
                                           List<String> uris,
                                           Boolean unique) {
        if (start.isAfter(end)) {
            throw new BadRequestException("Date error");
        }
        return statsRepositoryJPA.findByDate(start, end, uris, unique)
                .stream()
                .map(StatsMapper::toStatsResponseDto)
                .collect(Collectors.toList());
    }
}
