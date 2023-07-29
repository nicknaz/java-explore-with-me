package com.ewm.ewmmainservice.event.repository;

import com.ewm.ewmmainservice.event.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepositoryJPA extends JpaRepository<Location, Long> {
}
