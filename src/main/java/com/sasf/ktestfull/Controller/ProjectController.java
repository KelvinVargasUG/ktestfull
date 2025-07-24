package com.sasf.ktestfull.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sasf.ktestfull.Dto.ApiGenericResponse;
import com.sasf.ktestfull.Dto.PaginatedResponse;
import com.sasf.ktestfull.Dto.ProjectRequestDto;
import com.sasf.ktestfull.Dto.ProjectResponseDto;
import com.sasf.ktestfull.Service.IProjectService;
import com.sasf.ktestfull.Util.ResponseUtil;

import jakarta.validation.Valid;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/projects")
public class ProjectController {
    private final IProjectService projectService;

    public ProjectController(IProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping
    public ResponseEntity<ApiGenericResponse<ProjectResponseDto>> createProject(
            @RequestParam(required = true) String user,
            @Valid @RequestBody ProjectRequestDto request) {

        ProjectResponseDto createdProject = projectService.createProject(request, user);
        return ResponseUtil.created(null, createdProject);
    }

    @GetMapping
    public ResponseEntity<ApiGenericResponse<PaginatedResponse<ProjectResponseDto>>> getAllProject(
            @RequestParam(required = true) Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection) {

        PaginatedResponse<ProjectResponseDto> users = projectService.getProjectsByUser(page, size, sortBy,
                sortDirection, userId);
        return ResponseUtil.ok(null, users);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiGenericResponse<ProjectResponseDto>> updateProject(
            @PathVariable("id") Long idProject,
            @RequestParam(required = true) Long userId,
            @Valid @RequestBody ProjectRequestDto request) {

        ProjectResponseDto updatedProject = projectService.updateProject(idProject, request, userId);
        return ResponseUtil.ok(null, updatedProject);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiGenericResponse<Object>> deleteProject(
            @PathVariable Long id,
            @RequestParam Long userId) {

        projectService.deleteProject(id, userId);
        return ResponseUtil.ok("Project deleted successfully", null);
    }

}