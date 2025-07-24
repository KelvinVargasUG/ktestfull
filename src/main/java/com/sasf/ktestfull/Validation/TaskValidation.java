package com.sasf.ktestfull.Validation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.sasf.ktestfull.Constant.StatusConst;
import com.sasf.ktestfull.Dto.TaskRequestDto;
import com.sasf.ktestfull.Entity.Project;
import com.sasf.ktestfull.Entity.Task;
import com.sasf.ktestfull.Entity.User;
import com.sasf.ktestfull.Exception.AccessDeniedException;
import com.sasf.ktestfull.Exception.ResourceNotFoundException;
import com.sasf.ktestfull.Repository.UserRepository;

@Component
public class TaskValidation {

    private final UserRepository userRepository;

    public TaskValidation(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void validateCreateTaskRequest(TaskRequestDto request) {
        if (request == null) {
            throw new IllegalArgumentException("Task data is required");
        }

        if (request.getTitle() == null || request.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Task title is required");
        }

        if (request.getDescription() == null || request.getDescription().trim().isEmpty()) {
            throw new IllegalArgumentException("Task description is required");
        }

        if (request.getProjectId() == null || request.getProjectId() <= 0) {
            throw new IllegalArgumentException("Valid project ID is required");
        }

        if (request.getUserId() == null || request.getUserId().isEmpty()) {
            throw new IllegalArgumentException("At least one user must be assigned to the task");
        }

        Set<Long> uniqueUserIds = new HashSet<>(request.getUserId());
        if (uniqueUserIds.size() != request.getUserId().size()) {
            throw new IllegalArgumentException("Duplicate user assignments are not allowed");
        }
    }

    public void validateUserCanCreateTaskInProject(User user, Project project) {
        if (!project.getUser().getIdUser().equals(user.getIdUser())) {
            throw new AccessDeniedException("Only project owner can create tasks in this project");
        }
    }

    public List<User> validateAndGetAssignedUsers(List<Long> userIds) {
        List<User> users = new ArrayList<>();

        for (Long userId : userIds) {
            User user = userRepository.findByIdUserAndStatus(userId, StatusConst.ACTIVE)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found or inactivewith ID: " + userId));
            users.add(user);
        }
        return users;
    }

    public void updateTaskFields(Task task, TaskRequestDto request) {
        if (request.getTitle() != null) {
            task.setTitle(request.getTitle().trim());
        }

        if (request.getDescription() != null) {
            task.setDescription(request.getDescription().trim());
        }

        if (request.getStatus() != null) {
            task.setStatus(request.getStatus());
        }
    }
}
