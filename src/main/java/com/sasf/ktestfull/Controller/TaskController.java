package com.sasf.ktestfull.Controller;

import java.util.List;

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
import com.sasf.ktestfull.Dto.TaskRequestDto;
import com.sasf.ktestfull.Dto.TaskResponseDto;
import com.sasf.ktestfull.Service.ITaskService;
import com.sasf.ktestfull.Util.JwtUtil;
import com.sasf.ktestfull.Util.ResponseUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/tasks")
public class TaskController {
    private final ITaskService taskService;

    private final JwtUtil jwtUtil;

    public TaskController(ITaskService taskService, JwtUtil jwtUtil) {
        this.taskService = taskService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping
    public ResponseEntity<ApiGenericResponse<TaskResponseDto>> createTask(
            HttpServletRequest request,
            @Valid @RequestBody TaskRequestDto taskRequestDto) {

        Long userId = jwtUtil.extractUserIdFromCurrentToken(request);
        TaskResponseDto createdTask = taskService.createTask(taskRequestDto, userId);
        return ResponseUtil.created(null, createdTask);
    }

    @GetMapping
    public ResponseEntity<ApiGenericResponse<PaginatedResponse<TaskResponseDto>>> listUserTasks(
            HttpServletRequest request,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection) {

        Long userId = jwtUtil.extractUserIdFromCurrentToken(request);
        PaginatedResponse<TaskResponseDto> tasksPage = taskService.listUserTasks(userId, page, size, sortBy,
                sortDirection);
        return ResponseUtil.ok(null, tasksPage);
    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<ApiGenericResponse<List<TaskResponseDto>>> listTasksByProject(@PathVariable Long projectId) {

        List<TaskResponseDto> tasksPage = taskService.listTasksByProject(projectId);
        return ResponseUtil.ok(null, tasksPage);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiGenericResponse<TaskResponseDto>> updateTask(
            HttpServletRequest request,
            @PathVariable Long id,
            @Valid @RequestBody TaskRequestDto taskRequestDto) {

        Long userId = jwtUtil.extractUserIdFromCurrentToken(request);
        TaskResponseDto updatedTask = taskService.updateTask(id, taskRequestDto, userId);
        return ResponseUtil.ok(null, updatedTask);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiGenericResponse<Object>> deleteTask(
            HttpServletRequest request,
            @PathVariable Long id) {

        Long userId = jwtUtil.extractUserIdFromCurrentToken(request);
        taskService.deleteTask(id, userId);
        return ResponseUtil.ok("Task deleted successfully", null);
    }
}
