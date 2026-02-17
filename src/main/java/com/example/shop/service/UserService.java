package com.example.shop.service;

import com.example.shop.domian.User;
import com.example.shop.dto.mapper.MapperUser;
import com.example.shop.dto.user.UserDTO;
import com.example.shop.dto.user.UserRequestDTO;
import com.example.shop.dto.user.UserUpdateDTO;
import com.example.shop.event.user.UserEvent;
import com.example.shop.event.user.UserEventProducer;
import com.example.shop.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final MapperUser mapperUser;
    private final UserEventProducer userEventProducer;

    @Transactional(readOnly = true)
    public Page<UserDTO> getAllUser(Pageable pageable) {
        log.info("Started method getAllUser: page {}, size {}",
                pageable.getPageNumber(), pageable.getPageSize());
        Page<User> userList = userRepository.findAll(pageable);
        return userList.map(mapperUser::toResponse);
    }

    @Transactional(readOnly = true)
    public UserDTO getUserById(UUID id) {
        log.info("Start method getUserById {}.", id);

        var user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        UserEvent event = UserEvent.viewed(user);
        userEventProducer.sendUserEvent(event);

        return mapperUser.toResponse(user);
    }

    @Transactional
    public UserDTO createUser(UserRequestDTO userRequestDTO) {
        log.info("Start method createUser.");

        var userEntity = mapperUser.toEntity(userRequestDTO);
        userRepository.save(userEntity);

        UserEvent event = UserEvent.created(userEntity);
        userEventProducer.sendUserEvent(event);

        return mapperUser.toResponse(userEntity);
    }

    @Transactional
    public UserDTO updateUserById(UUID id, UserUpdateDTO userUpdateDTO) {
        log.info("Start method updateUserById {}", id);

        var entityUpdate = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        mapperUser.updateEntity(entityUpdate, userUpdateDTO);
        userRepository.save(entityUpdate);

        UserEvent event = UserEvent.updated(entityUpdate);
        userEventProducer.sendUserEvent(event);

        return mapperUser.toResponse(entityUpdate);
    }

    @Transactional
    public void deleteUserById(UUID id) {
        log.info("Start method deleteUserById {}", id);
        var deleteEntity = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        userRepository.delete(deleteEntity);
        UserEvent event = UserEvent.deleted(deleteEntity);
        userEventProducer.sendUserEvent(event);
    }
}
