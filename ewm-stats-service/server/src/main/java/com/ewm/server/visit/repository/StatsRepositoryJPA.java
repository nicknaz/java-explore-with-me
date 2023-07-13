package com.ewm.server.visit.repository;

import com.ewm.server.visit.model.StatsHit;
import com.ewm.server.visit.model.StatsResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatsRepositoryJPA extends JpaRepository<StatsHit, Long> {

    @Query("select new com.ewm.server.visit.model.StatsResponse(s.app, s.uri, count(s.ip)) " +
            " from StatsHit as s " +
            "where s.timestamp between ?1 and ?2 " +
            "and s.uri in ?3 " +
            "group by s.app, s.uri")
    List<StatsResponse> findByDate(LocalDateTime start,
                                   LocalDateTime end,
                                   List<String> uris,
                                   Boolean unique);
}
