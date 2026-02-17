package com.example.shop.dto.mapper;

import com.example.shop.domian.User;
import com.example.shop.dto.user.UserDTO;
import com.example.shop.dto.user.UserRequestDTO;
import com.example.shop.dto.user.UserUpdateDTO;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class MapperUser {

    public UserDTO toResponse(User user) {
        return new UserDTO(user.getId(),
                user.getEmail(),
                user.getUserName(),
                user.getFirstName(),
                user.getLastName(),
                user.getCreatedAt(),
                user.getUpdatedAt());
    }

    public User toEntity(UserRequestDTO userRequestDTO) {
        return User.builder()
                .email(userRequestDTO.email())
                .userName(userRequestDTO.userName())
                .firstName(userRequestDTO.firstName())
                .lastName(userRequestDTO.lastName())
                .createdAt(LocalDateTime.now())
                .build();
    }

    public void updateEntity(User user, UserUpdateDTO userUpdateDTO) {
        user.setEmail(userUpdateDTO.email());
        user.setUserName(userUpdateDTO.userName());
        user.setFirstName(userUpdateDTO.firstName());
        user.setLastName(userUpdateDTO.lastName());
        user.setUpdatedAt(LocalDateTime.now());
    }
}
