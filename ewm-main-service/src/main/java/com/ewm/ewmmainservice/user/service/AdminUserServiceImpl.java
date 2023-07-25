package com.ewm.ewmmainservice.user.service;

import com.ewm.ewmmainservice.exception.ConflictException;
import com.ewm.ewmmainservice.user.dto.mapper.UserMapper;
import com.ewm.ewmmainservice.user.dto.model.UserDto;
import com.ewm.ewmmainservice.user.repository.UserRepositoryJPA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class AdminUserServiceImpl implements AdminUserService{
    private UserRepositoryJPA userRepository;

    @Autowired
    public AdminUserServiceImpl(UserRepositoryJPA userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<UserDto> findUsers(List<Long> ids, Pageable page) {
        return userRepository.findUsers(ids, page)
                .stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserDto add(UserDto user) {
        if (userRepository.findUsersByEmail(user.getEmail()) != null
                || userRepository.findUsersByName(user.getName()) != null) {
            throw new ConflictException("Не уникальный юзер");
        }
        return UserMapper.toUserDto(userRepository.save(UserMapper.toUser(user)));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        userRepository.deleteById(id);
    }
}
