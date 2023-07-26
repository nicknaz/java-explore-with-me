package ru.practicum.visit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.visit.model.StatsHit;
import ru.practicum.visit.model.StatsResponse;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatsRepositoryJPA extends JpaRepository<StatsHit, Long> {

    @Query("select new ru.practicum.visit.model.StatsResponse(s.app, s.uri, " +
            "case when :unique = true then count(distinct s.ip) else count(s.ip) end over(partition by s.app, s.uri)) " +
            " from StatsHit as s " +
            "where s.timestamp between :start and :end " +
            "and (cast(:uris as integer) is null or s.uri in :uris) " +
            "order by case when :unique = true then count(distinct s.ip) else s end desc")
    List<StatsResponse> findByDate(@Param("start") LocalDateTime start,
                                   @Param("end") LocalDateTime end,
                                   @Param("uris") List<String> uris,
                                   @Param("unique") Boolean unique);
}
