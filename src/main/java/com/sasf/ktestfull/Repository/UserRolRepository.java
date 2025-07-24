package com.sasf.ktestfull.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sasf.ktestfull.Entity.UserRol;

@Repository
public interface UserRolRepository extends JpaRepository<UserRol, Long> {

}
