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

    @Query("SELECT u FROM User u WHERE u.userRole IS NOT NULL")
    List<User> findAllWithRoles();

    @Query("SELECT u FROM User u WHERE u.userRole IS NULL")
    List<User> findAllWithoutRoles();

    @Query("SELECT u FROM User u WHERE u.userRole IS NOT NULL AND u.organization.organizationId = ?1")
    List<User> findAllWithRolesAndByOrganization(int organizationId);

    @Query("SELECT u FROM User u WHERE u.userRole IS NOT NULL AND u.organization.organizationId = ?1 AND u.userId <> ?2")
    List<User> findEligibleCoOwnersOrganization(int organizationId, int userId);

    @Query("SELECT u FROM User u WHERE u.userRole IS NULL AND u.organization.organizationId = ?1")
    List<User> findAllWithoutRolesAndByOrganization(int organizationId);

    @Modifying
    @Query("UPDATE User u SET u.userRole = :userRole WHERE u.userId = :userId")
    void updateUserRole(@Param(value = "userId") int userId, @Param(value = "userRole") String userRole);

    @Modifying
    @Query("UPDATE User u SET u.employeeFirstName = :firstName, u.employeeLastName = :lastName, u.employeePhoneNumber = :phoneNumber WHERE u.userId = :userId")
    void updateUserDetails(@Param(value = "userId") int userId, @Param(value = "firstName") String firstName, @Param(value = "lastName") String lastName, @Param(value = "phoneNumber") String phoneNumber);

    @Modifying
    @Query("UPDATE User u SET u.userRole = NULL WHERE u.userId = :userId")
    void deleteUserRole(@Param(value = "userId") int userId);

    @Query("SELECT COUNT(u.userRole) FROM User u")
    int numberOfUsersWithRoles();

    @Query("SELECT COUNT(u.userRole) FROM User u WHERE u.organization.organizationId = ?1")
    int numberOfUsersWithRolesAndByOrganization(int organizationId);
}
