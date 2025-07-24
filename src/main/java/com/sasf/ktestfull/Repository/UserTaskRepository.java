package com.sasf.ktestfull.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sasf.ktestfull.Entity.UserTask;

@Repository
public interface UserTaskRepository extends JpaRepository<UserTask, Long> {

    List<UserTask> findByTaskIdTaskAndStatus(Long taskId, String active);

}
