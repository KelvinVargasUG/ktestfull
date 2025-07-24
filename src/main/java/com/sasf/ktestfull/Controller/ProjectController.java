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
import com.sasf.ktestfull.Util.JwtUtil;
import com.sasf.ktestfull.Util.ResponseUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/projects")
public class ProjectController {

    private final IProjectService projectService;

    private final JwtUtil jwtUtil;

    public ProjectController(IProjectService projectService, JwtUtil jwtUtil) {
        this.projectService = projectService;
        this.jwtUtil = jwtUtil;
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
            HttpServletRequest request,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection) {
        Long userId = jwtUtil.extractUserIdFromCurrentToken(request);
        PaginatedResponse<ProjectResponseDto> users = projectService.getProjectsByUser(page, size, sortBy,
                sortDirection, userId);
        return ResponseUtil.ok(null, users);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiGenericResponse<ProjectResponseDto>> updateProject(
            HttpServletRequest request,
            @PathVariable("id") Long idProject,
            @Valid @RequestBody ProjectRequestDto projectRequestDto) {
        Long userId = jwtUtil.extractUserIdFromCurrentToken(request);
        ProjectResponseDto updatedProject = projectService.updateProject(idProject, projectRequestDto, userId);
        return ResponseUtil.ok(null, updatedProject);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiGenericResponse<Object>> deleteProject(
            HttpServletRequest request,
            @PathVariable Long id) {

        Long userId = jwtUtil.extractUserIdFromCurrentToken(request);
        projectService.deleteProject(id, userId);
        return ResponseUtil.ok("Project deleted successfully", null);
    }

}