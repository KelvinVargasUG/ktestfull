package com.sasf.ktestfull.Mapper;

import org.springframework.stereotype.Component;

import com.sasf.ktestfull.Dto.ProjectRequestDto;
import com.sasf.ktestfull.Dto.ProjectResponseDto;
import com.sasf.ktestfull.Entity.Project;

@Component
public class ProjectMapper {

    private final UserMapper userMapper;

    public ProjectMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public Project toEntity(ProjectRequestDto requestDto) {
        if (requestDto == null) {
            return null;
        }

        Project project = new Project();
        project.setName(requestDto.getName());
        project.setDescription(requestDto.getDescription());
        return project;
    }

    public ProjectResponseDto toResponseDto(Project project) {
        if (project == null) {
            return null;
        }

        ProjectResponseDto dto = ProjectResponseDto
                .builder().idProject(project.getIdProject())
                .name(project.getName())
                .description(project.getDescription())
                .createdAt(project.getCreatedAt())
                .status(project.getStatus()).build();

        if (project.getUser() != null) {
            dto.setOwner(userMapper.toResponseDto(project.getUser()));
        }
        return dto;
    }

    public ProjectRequestDto toRequestDto(Project project) {
        if (project == null) {
            return null;
        }

        ProjectRequestDto dto = ProjectRequestDto
                .builder()
                .owner(project.getUser().getIdUser())
                .name(project.getName())
                .description(project.getDescription())
                .status(project.getStatus()).build();
        return dto;
    }
}
