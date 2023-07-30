package com.ewm.ewmmainservice.user.service;

import com.ewm.ewmmainservice.exception.NotFoundedException;
import com.ewm.ewmmainservice.user.model.User;
import com.ewm.ewmmainservice.user.repository.UserRepositoryJPA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PublicUserServiceImpl implements PublicUserService {
    private UserRepositoryJPA userRepository;

    @Autowired
    public PublicUserServiceImpl(UserRepositoryJPA userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void subscribe(Long userId, Long trackedId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundedException("Пользователь не найден"));

        User tracked = userRepository.findById(trackedId)
                .orElseThrow(() -> new NotFoundedException("Подписант не найден"));

        user.getTracked().add(tracked);

        userRepository.save(user);
    }
}
