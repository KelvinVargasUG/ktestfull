package com.sasf.ktestfull.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sasf.ktestfull.Dto.ApiGenericResponse;
import com.sasf.ktestfull.Dto.PaginatedResponse;
import com.sasf.ktestfull.Dto.UserRequestDto;
import com.sasf.ktestfull.Dto.UserResponseDto;
import com.sasf.ktestfull.Service.IUserService;
import com.sasf.ktestfull.Util.ResponseUtil;

import jakarta.validation.Valid;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/users")
public class UserController {

    private final IUserService userService;

    public UserController(IUserService userService) {
        this.userService = userService;
    }

    /**
     * Retrieves a paginated list of users.
     *
     * @param page          the page number to retrieve (default is 0)
     * @param size          the number of users per page (default is 10, max is 100)
     * @param sortBy        the field to sort by (default is "createdAt")
     * @param sortDirection the direction of sorting (default is "DESC")
     * @return a paginated response containing user data
     */
    @GetMapping
    public ResponseEntity<ApiGenericResponse<PaginatedResponse<UserResponseDto>>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection) {

        PaginatedResponse<UserResponseDto> users = userService.getAllUsers(page, size, sortBy, sortDirection);
        return ResponseUtil.ok(null, users);
    }

    /**
     * Retrieves a user by their ID.
     *
     * @param id the ID of the user to retrieve
     * @return a response containing the user data
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiGenericResponse<UserResponseDto>> getUserById(@PathVariable Long id) {

        UserResponseDto user = userService.getUserById(id);
        return ResponseUtil.ok(null, user);
    }

    @PostMapping
    public ResponseEntity<ApiGenericResponse<UserResponseDto>> createUser(
            @RequestParam(required = true) String user,
            @Valid @RequestBody UserRequestDto request) {

        UserResponseDto createdUser = userService.createUser(request, user);
        return ResponseUtil.created(null, createdUser);
    }
}
