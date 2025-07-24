package com.sasf.ktestfull.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sasf.ktestfull.Entity.Project;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    @Query("SELECT p FROM Project p WHERE p.status IN :statuses AND p.user.idUser = :userId")
    Page<Project> findByStatusAndIdUser(@Param("statuses") List<String> statuses, Long userId, Pageable pageable);

    @Query("SELECT p FROM Project p WHERE p.status = :status AND p.idProject = :idProject")
    Optional<Project> findByIdProjectAndStatus(Long idProject, String status);

    @Query("SELECT p FROM Project p WHERE p.status in :statuses AND p.idProject = :idProject")
    Optional<Project> findByIdProjectAndStatus(Long idProject, @Param("statuses") List<String> status);

}
