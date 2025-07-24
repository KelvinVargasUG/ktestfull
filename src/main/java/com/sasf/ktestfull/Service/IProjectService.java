package com.sasf.ktestfull.Service;

import org.springframework.stereotype.Service;

import com.sasf.ktestfull.Dto.PaginatedResponse;
import com.sasf.ktestfull.Dto.ProjectRequestDto;
import com.sasf.ktestfull.Dto.ProjectResponseDto;

@Service
public interface IProjectService {
    public ProjectResponseDto createProject(ProjectRequestDto request, String user);

    public PaginatedResponse<ProjectResponseDto> getProjectsByUser(int page, int size, String sortBy,
            String sortDirection, Long userId);

    public ProjectResponseDto updateProject(Long idProject, ProjectRequestDto request, Long userId);

    public void deleteProject(Long idProject, Long userId);

}
