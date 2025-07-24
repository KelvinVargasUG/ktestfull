package com.sasf.ktestfull.Service.Impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sasf.ktestfull.Constant.StatusConst;
import com.sasf.ktestfull.Dto.PaginatedResponse;
import com.sasf.ktestfull.Dto.TaskRequestDto;
import com.sasf.ktestfull.Dto.TaskResponseDto;
import com.sasf.ktestfull.Entity.Project;
import com.sasf.ktestfull.Entity.Task;
import com.sasf.ktestfull.Entity.User;
import com.sasf.ktestfull.Entity.UserTask;
import com.sasf.ktestfull.Exception.ResourceNotFoundException;
import com.sasf.ktestfull.Mapper.TaskMapper;
import com.sasf.ktestfull.Mapper.UserMapper;
import com.sasf.ktestfull.Repository.ProjectRepository;
import com.sasf.ktestfull.Repository.TaskRepository;
import com.sasf.ktestfull.Repository.UserTaskRepository;
import com.sasf.ktestfull.Service.ITaskService;
import com.sasf.ktestfull.Service.IUserTaskService;
import com.sasf.ktestfull.Validation.TaskValidation;
import com.sasf.ktestfull.Validation.UserValidation;

@Service
public class TaskServiceImpl implements ITaskService {

    private final TaskRepository taskRepository;

    private final ProjectRepository projectRepository;

    private final IUserTaskService userTaskService;

    private final UserTaskRepository userTaskRepository;

    private final TaskValidation taskValidation;

    private final TaskMapper taskMapper;

    private final UserMapper userMapper;

    private final UserValidation userValidation;

    public TaskServiceImpl(ProjectRepository projectRepository, TaskMapper taskMapper, TaskRepository taskRepository,
            UserMapper userMapper, UserValidation userValidation, TaskValidation taskValidation,
            IUserTaskService iUserTaskService, UserTaskRepository userTaskRepository,
            IUserTaskService userTaskService) {
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
        this.userTaskService = userTaskService;
        this.userTaskRepository = userTaskRepository;
        this.taskValidation = taskValidation;
        this.taskMapper = taskMapper;
        this.userMapper = userMapper;
        this.userValidation = userValidation;
    }

    @Override
    @Transactional
    public TaskResponseDto createTask(TaskRequestDto request, Long userId) {

        User creatorUser = userValidation.validateAndGetUser(userId);

        taskValidation.validateCreateTaskRequest(request);

        Project project = projectRepository.findByIdProjectAndStatus(request.getProjectId(), StatusConst.GENERIC_STATUS)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Project not found with ID:" + request.getProjectId()));

        taskValidation.validateUserCanCreateTaskInProject(creatorUser, project);

        List<User> assignedUsers = taskValidation.validateAndGetAssignedUsers(request.getUserId());

        Task task = taskMapper.toEntity(request);
        task.setProject(project);
        String status = request.getStatus() != null ? request.getStatus() : StatusConst.PENDING;
        task.setStatus(status);

        Task savedTask = taskRepository.save(task);

        userTaskService.createUserTaskAssignments(savedTask, assignedUsers);

        TaskResponseDto response = taskMapper.toResponseDto(savedTask);

        response.setAssignedUsersTask(assignedUsers.stream()
                .map(userMapper::toResponseDto)
                .collect(Collectors.toList()));

        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public PaginatedResponse<TaskResponseDto> listUserTasks(Long userId, int page, int size, String sortBy,
            String sortDirection) {

        userValidation.validateAndGetUser(userId);

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

        Page<Task> tasksPage = taskRepository.findTasksByUserId(userId, StatusConst.TASK_STATUS, pageable);
        Page<TaskResponseDto> responsePage = tasksPage.map(task -> taskMapper.toResponseDto(task));

        return PaginatedResponse.of(responsePage);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskResponseDto> listTasksByProject(Long projectId) {
        Project project = projectRepository.findByIdProjectAndStatus(projectId, StatusConst.ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException("Proyect not found with ID: " + projectId));
        return taskMapper.toResponseDtoList(project.getTasks().stream()
                .filter(task -> StatusConst.TASK_STATUS.contains(task.getStatus()))
                .collect(Collectors.toList()));
    }

    @Override
    @Transactional
    public TaskResponseDto updateTask(Long taskId, TaskRequestDto request, Long userId) {

        User user = userValidation.validateAndGetUser(userId);

        Task existingTask = taskRepository.findByIdTaskAndStatusIn(taskId, StatusConst.TASK_STATUS)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with ID: " + taskId));

        taskValidation.updateTaskFields(existingTask, request);

        List<User> usersAsigned = new ArrayList<>();
        if (request.getUserId() != null && !request.getUserId().isEmpty()) {
            List<Long> uniqueUserIds = request.getUserId().stream()
                    .distinct()
                    .collect(Collectors.toList());

            usersAsigned = taskValidation.validateAndGetAssignedUsers(uniqueUserIds);
        }

        existingTask.setCreatedBy(user.getUsername());

        Task updatedTask = taskRepository.save(existingTask);

        Set<Long> alreadyAssignedIds = updatedTask.getUserTasks().stream()
                .map(userTask -> userTask.getUser().getIdUser())
                .collect(Collectors.toSet());

        List<User> newAssignments = usersAsigned.stream()
                .filter(userFilter -> !alreadyAssignedIds.contains(user.getIdUser()))
                .collect(Collectors.toList());

        userTaskService.createUserTaskAssignments(updatedTask, newAssignments);

        TaskResponseDto response = taskMapper.toResponseDto(updatedTask);
        List<User> assignedUsers = getUsersAssignedToTask(updatedTask.getIdTask());
        response.setAssignedUsersTask(assignedUsers.stream()
                .map(userMapper::toResponseDto)
                .collect(Collectors.toList()));

        return response;
    }

    private List<User> getUsersAssignedToTask(Long taskId) {
        List<UserTask> userTasks = userTaskRepository.findByTaskIdTaskAndStatus(taskId, StatusConst.ACTIVE);
        return userTasks.stream()
                .map(UserTask::getUser)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteTask(Long taskId, Long userId) {

        Task existingTask = taskRepository.findByIdTaskAndStatusIn(taskId, StatusConst.TASK_STATUS)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with ID: " + taskId));

        TaskRequestDto requestDto = taskMapper.toRequestDto(existingTask);
        requestDto.setStatus(StatusConst.REMOVED);
        this.updateTask(taskId, requestDto, userId);
    }
}