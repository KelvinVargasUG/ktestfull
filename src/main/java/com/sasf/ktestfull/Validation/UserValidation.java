package com.sasf.ktestfull.Validation;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.sasf.ktestfull.Constant.StatusConst;
import com.sasf.ktestfull.Entity.Rol;
import com.sasf.ktestfull.Entity.User;
import com.sasf.ktestfull.Entity.UserRol;
import com.sasf.ktestfull.Exception.DuplicateResourceException;
import com.sasf.ktestfull.Exception.ResourceNotFoundException;
import com.sasf.ktestfull.Repository.RolRepository;
import com.sasf.ktestfull.Repository.UserRepository;
import com.sasf.ktestfull.Repository.UserRolRepository;

@Component
public class UserValidation {

    @Value("${user.default-role}")
    private String defaultRole;

    private final RolRepository rolRepository;

    private final UserRolRepository userRolRepository;

    private final UserRepository userRepository;

    public UserValidation(UserRepository userRepository, RolRepository rolRepository,
            UserRolRepository userRolRepository) {
        this.rolRepository = rolRepository;
        this.userRolRepository = userRolRepository;
        this.userRepository = userRepository;
    }

    public User validateAndGetUser(Long userId) {

        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("The user ID must be a positive number.");
        }

        User user = userRepository.findByIdUserAndStatus(userId, StatusConst.ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        return user;
    }

    /**
     * Assigns a default role to the user.
     *
     * @param savedUser the user to assign the default role to
     */
    public Rol assignDefaultRole(User user) {
        Rol userRole = rolRepository.findByNameAndStatus(defaultRole, StatusConst.ACTIVE)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Default role not found: " + defaultRole));
        UserRol userRol = new UserRol();
        userRol.setUser(user);
        userRol.setRol(userRole);
        userRol.setStatus(StatusConst.ACTIVE);
        userRolRepository.save(userRol);
        return userRole;

    }

    /**
     * Validates that the username and email are unique.
     *
     * @param username the username to check
     * @param email    the email to check
     * @throws DuplicateResourceException if the username or email already exists
     */
    public void validateUserUniqueness(String username, String email) {
        if (userRepository.existsByUsernameAndStatus(username, StatusConst.GENERIC_STATUS)) {
            throw new DuplicateResourceException("The username is already taken: " + username);
        }
        if (userRepository.existsByEmailAndStatus(email, StatusConst.GENERIC_STATUS)) {
            throw new DuplicateResourceException("The email is already taken: " + email);
        }
    }
}
