package com.sasf.ktestfull.Validation;

import org.springframework.stereotype.Component;

import com.sasf.ktestfull.Dto.ProjectRequestDto;
import com.sasf.ktestfull.Entity.Project;
import com.sasf.ktestfull.Entity.User;
import com.sasf.ktestfull.Exception.AccessDeniedException;

@Component
public class ProjectValidation {

    public void validateProjectOwnership(Project project, Long currentUserId) {
        if (currentUserId == null) {
            throw new IllegalArgumentException("The currentUserId is necesary");
        }

        if (project.getUser().getIdUser() != currentUserId) {
            throw new AccessDeniedException("You do not have permission to update this project");
        }
    }

    public void updateProjectFields(Project existingProject, ProjectRequestDto request, User newAssignedUsers) {
        if (request.getName() != null) {
            existingProject.setName(request.getName().trim());
        }

        if (request.getDescription() != null) {
            existingProject.setDescription(request.getDescription().trim());
        }

        if (newAssignedUsers != null) {
            existingProject.setUser(newAssignedUsers);
        }

        if (request.getStatus() != null) {
            existingProject.setStatus(request.getStatus());
        }
    }
}
