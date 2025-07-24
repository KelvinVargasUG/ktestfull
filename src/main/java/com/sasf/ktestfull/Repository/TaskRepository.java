package com.sasf.ktestfull.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sasf.ktestfull.Entity.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query("SELECT DISTINCT t FROM Task t " +
            "JOIN t.userTasks ut " +
            "WHERE ut.user.idUser = :userId AND t.status IN :statuses AND ut.status IN :statuses")
    Page<Task> findTasksByUserId(Long userId, @Param("statuses") List<String> genericStatus, Pageable pageable);

    Page<Task> findByProjectIdProjectAndStatus(Long projectId, String active, Pageable pageable);

    Optional<Task> findByIdTaskAndStatusIn(Long taskId, List<String> genericStatus);

}
