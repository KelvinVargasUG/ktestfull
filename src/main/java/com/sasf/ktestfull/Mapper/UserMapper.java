package com.sasf.ktestfull.Mapper;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.sasf.ktestfull.Dto.UserRequestDto;
import com.sasf.ktestfull.Dto.UserResponseDto;
import com.sasf.ktestfull.Entity.User;

@Component
public class UserMapper {

    /**
     * Converts a User entity to a UserResponseDto.
     * 
     * @param user
     * @return UserResponseDto
     */
    public UserResponseDto toResponseDto(User user) {
        if (user == null) {
            return null;
        }

        return UserResponseDto.builder().idUser(user.getIdUser())
                .username(user.getUsername())
                .email(user.getEmail())
                .rol(user.getRol().stream().map(rol -> rol.getName()).collect(Collectors.toList()))
                .status(user.getStatus())
                .build();
    }

    public List<UserResponseDto> toResponseDtoList(List<User> users) {
        if (users == null || users.isEmpty()) {
            return Collections.emptyList();
        }

        return users.stream()
                    .map(this::toResponseDto)
                    .collect(Collectors.toList());
    }

    /**
     * Converts a UserRequestDto to a User entity.
     * 
     * @param request
     * @return User
     */
    public User toEntity(UserRequestDto request) {
        if (request == null) {
            return null;
        }
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        return user;
    }
}