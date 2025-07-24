package com.sasf.ktestfull.Service;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.sasf.ktestfull.Entity.Task;
import com.sasf.ktestfull.Entity.User;

@Repository
public interface IUserTaskService {
    public void createUserTaskAssignments(Task task, List<User> users);
}
