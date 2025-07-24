package com.sasf.ktestfull.Mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.sasf.ktestfull.Dto.TaskRequestDto;
import com.sasf.ktestfull.Dto.TaskResponseDto;
import com.sasf.ktestfull.Entity.Task;
import com.sasf.ktestfull.Entity.User;
import com.sasf.ktestfull.Entity.UserTask;

@Component
public class TaskMapper {

    private final UserMapper userMapper;

    public TaskMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public Task toEntity(TaskRequestDto requestDto) {
        if (requestDto == null) {
            return null;
        }

        Task task = new Task();
        task.setTitle(requestDto.getTitle());
        task.setDescription(requestDto.getDescription());
        return task;
    }

    public TaskResponseDto toResponseDto(Task task) {
        if (task == null) {
            return null;
        }

        List<User> users = new ArrayList<>();

        if (task.getUserTasks() != null) {
            task.getUserTasks().forEach(userTask -> users.add(userTask.getUser()));
        }

        TaskResponseDto dto = TaskResponseDto.builder()
                .idTask(task.getIdTask())
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus())
                .assignedUsersTask(userMapper.toResponseDtoList(users))
                .build();
        return dto;
    }

    public TaskRequestDto toRequestDto(Task task) {
        if (task == null) {
            return null;
        }

        List<User> users = new ArrayList<>();

        for (UserTask userTask : task.getUserTasks()) {
            users.add(userTask.getUser());
        }

        TaskRequestDto dto = TaskRequestDto.builder()
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus())
                .projectId(task.getProject().getIdProject())
                .build();
        return dto;
    }

    public List<TaskResponseDto> toResponseDtoList(List<Task> tasks) {
        if (tasks == null || tasks.isEmpty()) {
            return new ArrayList<>();
        }

        return tasks.stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }
}
