package com.tracom.cohort5project.Repositories;

import com.tracom.cohort5project.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {

    @Query("SELECT u FROM User u WHERE u.employeeEmailAddress = ?1")
    User findByEmployeeEmailAddress(String emailAddress);

    @Query("SELECT u FROM User u WHERE NOT u.userRole = 'NONE'")
    List<User> findAllWithRoles();

    @Modifying
    @Query("UPDATE User u SET u.userRole = :userRole WHERE u.userId = :userId")
    void updateUserRole(@Param(value = "userId") int userId, @Param(value = "userRole") String userRole);

    @Modifying
    @Query("UPDATE User u SET u.userRole = 'NONE' WHERE u.userId = :userId")
    void deleteUserRole(@Param(value = "userId") int userId);
}
