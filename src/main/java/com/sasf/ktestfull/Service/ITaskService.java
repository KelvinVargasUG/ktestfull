package com.sasf.ktestfull.Service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sasf.ktestfull.Dto.PaginatedResponse;
import com.sasf.ktestfull.Dto.TaskRequestDto;
import com.sasf.ktestfull.Dto.TaskResponseDto;

@Service
public interface ITaskService {

    public TaskResponseDto createTask(TaskRequestDto request, Long userId);

    public PaginatedResponse<TaskResponseDto> listUserTasks(Long userId, int page, int size, String sortBy,
            String sortDirection);

    public List<TaskResponseDto> listTasksByProject(Long projectId);

    public TaskResponseDto updateTask(Long taskId, TaskRequestDto request, Long userId);

    public void deleteTask(Long taskId, Long userId);

}
