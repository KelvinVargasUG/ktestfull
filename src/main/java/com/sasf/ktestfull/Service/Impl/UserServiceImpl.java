package com.sasf.ktestfull.Service.Impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.sasf.ktestfull.Constant.StatusConst;
import com.sasf.ktestfull.Dto.PaginatedResponse;
import com.sasf.ktestfull.Dto.UserRequestDto;
import com.sasf.ktestfull.Dto.UserResponseDto;
import com.sasf.ktestfull.Entity.Rol;
import com.sasf.ktestfull.Entity.User;
import com.sasf.ktestfull.Exception.ResourceNotFoundException;
import com.sasf.ktestfull.Mapper.UserMapper;
import com.sasf.ktestfull.Repository.UserRepository;
import com.sasf.ktestfull.Service.IUserService;
import com.sasf.ktestfull.Validation.UserValidation;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserServiceImpl implements IUserService {

    private final UserValidation userValidation;

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, UserValidation userValidation) {
        this.userValidation = userValidation;
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    /**
     * Creates a new user.
     *
     * @param request the user request DTO containing user data
     * @return the created user response DTO
     */
    @Override
    public UserResponseDto createUser(UserRequestDto request, String user) {

        userValidation.validateUserUniqueness(request.getUsername(), request.getEmail());
        User userCreate = userMapper.toEntity(request);
        // user.setPassword(passwordEncoder.encode(request.getPassword()));
        userCreate.setStatus(StatusConst.ACTIVE);
        userCreate.setCreatedBy(user);
        User savedUser = userRepository.save(userCreate);
        Rol assignedRole = userValidation.assignDefaultRole(savedUser);
        savedUser.setRol(List.of(assignedRole));
        UserResponseDto response = userMapper.toResponseDto(savedUser);
        return response;
    }

    /**
     * Retrieves all users with pagination and sorting.
     *
     * @param page          the page number to retrieve
     * @param size          the number of items per page
     * @param sortBy        the field to sort by
     * @param sortDirection the direction of sorting (ASC or DESC)
     * @return a paginated response containing user data
     */
    @Override
    public PaginatedResponse<UserResponseDto> getAllUsers(int page, int size, String sortBy, String sortDirection) {
        if (page < 0)
            page = 0;
        if (size <= 0)
            size = 10;
        if (size > 100)
            size = 100;
        if (sortBy == null || sortBy.trim().isEmpty())
            sortBy = "createdAt";
        if (sortDirection == null)
            sortDirection = "DESC";

        Sort.Direction direction = Sort.Direction.fromString(sortDirection.toUpperCase());
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<User> usersPage = userRepository.findByStatus(StatusConst.GENERIC_STATUS, pageable);
        Page<UserResponseDto> responsePage = usersPage.map(user -> userMapper.toResponseDto(user));
        return PaginatedResponse.of(responsePage);
    }

    /**
     * Retrieves a user by their ID.
     *
     * @param id the ID of the user to retrieve
     * @return the user response DTO
     */
    @Override
    public UserResponseDto getUserById(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("The user ID must be a positive number.");
        }
        User user = userRepository.findByIdUserAndStatuses(id, StatusConst.GENERIC_STATUS)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));

        return userMapper.toResponseDto(user);
    }

}
