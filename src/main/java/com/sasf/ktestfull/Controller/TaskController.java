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
import com.sasf.ktestfull.Util.ResponseUtil;

import jakarta.validation.Valid;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/tasks")
public class TaskController {
    private final ITaskService taskService;

    public TaskController(ITaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public ResponseEntity<ApiGenericResponse<TaskResponseDto>> createTask(
            @Valid @RequestBody TaskRequestDto request,
            @RequestParam Long userId) {

        TaskResponseDto createdTask = taskService.createTask(request, userId);
        return ResponseUtil.created(null, createdTask);
    }

    @GetMapping
    public ResponseEntity<ApiGenericResponse<PaginatedResponse<TaskResponseDto>>> listUserTasks(
            @RequestParam Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection) {

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
            @PathVariable Long id,
            @Valid @RequestBody TaskRequestDto request,
            @RequestParam Long userId) {

        TaskResponseDto updatedTask = taskService.updateTask(id, request, userId);
        return ResponseUtil.ok(null, updatedTask);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiGenericResponse<Object>> deleteTask(
            @PathVariable Long id,
            @RequestParam Long userId) {

        taskService.deleteTask(id, userId);
        return ResponseUtil.ok("Task deleted successfully", null);
    }
}
