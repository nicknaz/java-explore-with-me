package com.ewm.ewmmainservice.compilation.repository;

import com.ewm.ewmmainservice.compilation.model.Compilation;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompilationRepositoryJPA extends JpaRepository<Compilation, Long> {

    @Query(value = "select com from Compilation as com " +
            "where (com.pinned = (?1) or (?1) is null) " +
            "order by com.id desc")
    List<Compilation> findAllByPinned(Boolean pinned, Pageable page);

}
