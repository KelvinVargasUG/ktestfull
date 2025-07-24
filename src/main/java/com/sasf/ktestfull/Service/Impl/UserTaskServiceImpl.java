package com.sasf.ktestfull.Service.Impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.sasf.ktestfull.Constant.StatusConst;
import com.sasf.ktestfull.Entity.Task;
import com.sasf.ktestfull.Entity.User;
import com.sasf.ktestfull.Entity.UserTask;
import com.sasf.ktestfull.Repository.UserTaskRepository;
import com.sasf.ktestfull.Service.IUserTaskService;

@Service
public class UserTaskServiceImpl implements IUserTaskService {

    private final UserTaskRepository userTaskRepository;

    public UserTaskServiceImpl(UserTaskRepository userTaskRepository) {
        this.userTaskRepository = userTaskRepository;
    }

    @Override
    public void createUserTaskAssignments(Task task, List<User> users) {
        List<UserTask> userTasks = new ArrayList<>();

        for (User user : users) {
            UserTask userTask = new UserTask();
            userTask.setUser(user);
            userTask.setTask(task);
            userTask.setStatus(StatusConst.ACTIVE);
            userTasks.add(userTask);
        }

        userTaskRepository.saveAll(userTasks);
    }

}
