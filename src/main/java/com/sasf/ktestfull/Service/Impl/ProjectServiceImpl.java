package com.sasf.ktestfull.Service.Impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.sasf.ktestfull.Constant.StatusConst;
import com.sasf.ktestfull.Dto.PaginatedResponse;
import com.sasf.ktestfull.Dto.ProjectRequestDto;
import com.sasf.ktestfull.Dto.ProjectResponseDto;
import com.sasf.ktestfull.Entity.Project;
import com.sasf.ktestfull.Entity.User;
import com.sasf.ktestfull.Exception.ResourceNotFoundException;
import com.sasf.ktestfull.Mapper.ProjectMapper;
import com.sasf.ktestfull.Mapper.UserMapper;
import com.sasf.ktestfull.Repository.ProjectRepository;
import com.sasf.ktestfull.Service.IProjectService;
import com.sasf.ktestfull.Validation.ProjectValidation;
import com.sasf.ktestfull.Validation.UserValidation;

@Service
public class ProjectServiceImpl implements IProjectService {

    private final UserMapper userMapper;

    private final UserValidation userValidation;

    private final ProjectMapper projectMapper;

    private final ProjectRepository projectRepository;

    private final ProjectValidation projectValidation;

    public ProjectServiceImpl(UserValidation userValidation, ProjectMapper projectMapper,
            ProjectRepository projectRepository, ProjectValidation projectValidation, UserMapper userMapper) {
        this.userValidation = userValidation;
        this.projectMapper = projectMapper;
        this.projectRepository = projectRepository;
        this.projectValidation = projectValidation;
        this.userMapper = userMapper;
    }

    @Override
    public ProjectResponseDto createProject(ProjectRequestDto request, String user) {

        User assignedUser = userValidation.validateAndGetUser(request.getOwner());
        Project project = projectMapper.toEntity(request);
        User projectOwner = assignedUser;
        project.setStatus(StatusConst.ACTIVE);
        project.setCreatedBy(user);
        project.setUser(projectOwner);

        Project savedProject = projectRepository.save(project);
        return projectMapper.toResponseDto(savedProject);
    }

    @Override
    public PaginatedResponse<ProjectResponseDto> getProjectsByUser(int page, int size, String sortBy,
            String sortDirection, Long userId) {
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

        Page<Project> projectPage = projectRepository.findByStatusAndIdUser(StatusConst.GENERIC_STATUS, userId,
                pageable);
        Page<ProjectResponseDto> responsePage = projectPage.map(user -> projectMapper.toResponseDto(user));
        return PaginatedResponse.of(responsePage);
    }

    @Override
    public ProjectResponseDto updateProject(Long idProject, ProjectRequestDto request, Long userId) {

        userValidation.validateAndGetUser(userId);

        Project existingProject = projectRepository.findByIdProjectAndStatus(idProject, StatusConst.ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException("Proyect not found with ID: " + idProject));

        projectValidation.validateProjectOwnership(existingProject, userId);

        User newAssignedUsers = null;
        if (request.getOwner() != null && request.getOwner() > 0) {
            newAssignedUsers = userValidation.validateAndGetUser(request.getOwner());
        }

        projectValidation.updateProjectFields(existingProject, request, newAssignedUsers);

        Project updatedProject = projectRepository.save(existingProject);

        ProjectResponseDto response = projectMapper.toResponseDto(updatedProject);

        if (newAssignedUsers != null) {
            response.setOwner(userMapper.toResponseDto(newAssignedUsers));
        }

        return response;
    }

    @Override
    public void deleteProject(Long idProject, Long userId) {
        Project existingProject = projectRepository.findByIdProjectAndStatus(idProject, StatusConst.GENERIC_STATUS)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with ID: " + idProject));
        ProjectRequestDto projectRequestDto = projectMapper.toRequestDto(existingProject);
        projectRequestDto.setStatus(StatusConst.REMOVED);
        this.updateProject(existingProject.getIdProject(), projectRequestDto, userId);
    }

}
