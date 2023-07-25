package com.ewm.ewmmainservice.user.repository;

import com.ewm.ewmmainservice.user.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepositoryJPA extends JpaRepository<User, Long> {
    @Query(value = "select us from User as us " +
            "where (us.id in (?1) or (?1) is null) " +
            "order by us.id asc")
    List<User> findUsers(List<Long> ids, Pageable page);
    User findUsersByEmail(String email);
    User findUsersByName(String name);
}
