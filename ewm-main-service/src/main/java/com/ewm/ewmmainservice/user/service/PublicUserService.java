package com.ewm.ewmmainservice.user.service;

import org.springframework.stereotype.Service;

@Service
public interface PublicUserService {

    void subscribe(Long userId, Long trackedId);

}
