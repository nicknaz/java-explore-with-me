package com.ewm.ewmmainservice.user.service;

import com.ewm.ewmmainservice.user.dto.model.UserDto;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PublicUserService {

    void subscribe(Long userId, Long trackedId);

}
