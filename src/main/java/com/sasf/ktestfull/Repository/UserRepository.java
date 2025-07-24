package com.sasf.ktestfull.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sasf.ktestfull.Entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Finds users by their status.
     *
     * @param status   the status of the users to find
     * @param pageable pagination information
     * @return a page of users with the specified status
     */
    @Query("SELECT u FROM User u WHERE u.status IN :statuses")
    public Page<User> findByStatus(@Param("statuses") List<String> statuses, Pageable pageable);

    /**
     * Finds a user by their ID and status.
     *
     * @param idUser the ID of the user
     * @param status the status of the user
     * @return an Optional containing the user if found, or empty if not found
     */
    @Query("SELECT u FROM User u WHERE u.idUser = :idUser AND u.status IN :statuses")
    public Optional<User> findByIdUserAndStatuses(@Param("idUser") Long idUser,
            @Param("statuses") List<String> statuses);

    @Query("SELECT COUNT(u) > 0 FROM User u WHERE u.username = :username AND u.status IN :statuses")
    public boolean existsByUsernameAndStatus(String username, @Param("statuses") List<String> validStatuses);

    @Query("SELECT COUNT(u) > 0 FROM User u WHERE u.email = :email AND u.status IN :statuses")
    public boolean existsByEmailAndStatus(String email, @Param("statuses") List<String> validStatuses);

    public Optional<User> findByIdUserAndStatus(Long userId, String active);
}
