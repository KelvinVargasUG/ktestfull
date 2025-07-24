package com.sasf.ktestfull.Service;

import org.springframework.stereotype.Service;

import com.sasf.ktestfull.Dto.PaginatedResponse;
import com.sasf.ktestfull.Dto.UserRequestDto;
import com.sasf.ktestfull.Dto.UserResponseDto;

@Service
public interface IUserService {
    public UserResponseDto createUser(UserRequestDto request, String user);

    public PaginatedResponse<UserResponseDto> getAllUsers(int page, int size, String sortBy, String sortDirection);

    public UserResponseDto getUserById(Long id);

    public UserResponseDto getAuthenticatedUserId(String email);

}