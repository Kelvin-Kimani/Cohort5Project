package com.tracom.cohort5project.Services;

import com.tracom.cohort5project.Entities.User;
import com.tracom.cohort5project.Exceptions.UserNotFoundException;
import com.tracom.cohort5project.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService {

    public static final int MAX_FAILED_ATTEMPTS = 4;
    private static final long LOCK_TIME_DURATION = 30 * 60 * 1000; //30 Minutes in milliseconds

    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    /*
    CREATE
    Implementing HTTP POST
    */
    public void createUser(User user) throws IllegalStateException{
        Optional<User> userByEmail = Optional.ofNullable(userRepository.findByEmployeeEmailAddress(user.getEmployeeEmailAddress()));
        if (userByEmail.isPresent()){
            throw new IllegalStateException("Account with the same email exists!");
        }
        userRepository.save(user);
    }

    public void createSystemUserById(int id,
                                 String role){
        userRepository.updateUserRole(id, role);
    }


    /*READ*/
    public List<User> getUsers(){
        return userRepository.findAll();
    }
    public List<User> getUsersWithRoles() {
        return userRepository.findAllWithRoles();
    }

    public List<User> getUsersWithRolesAndByOrganization(int organizationId) {
        return userRepository.findAllWithRolesAndByOrganization(organizationId);
    }

    public List<User> getUsersWithoutRolesAndByOrganization(int organizationId) {
        return userRepository.findAllWithoutRolesAndByOrganization(organizationId);
    }

    public List<User> getUsersWithoutRoles() {
        return userRepository.findAllWithoutRoles();
    }

    public User getUserById(int id) {
        return userRepository.findById(id)
                .orElseThrow(()-> new IllegalStateException("User with id " + id + " does not exist."));
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmployeeEmailAddress(email);
    }

    public int numberOfUsersWithRoles(){
        return userRepository.numberOfUsersWithRoles();
    }

    public int numberOfUsersWithRolesAndByOrganization(int organizationId){
        return userRepository.numberOfUsersWithRolesAndByOrganization(organizationId);
    }

    public List<User> getEligibleCoOwners(int organizationId, int userId){
        return userRepository.findEligibleCoOwnersOrganization(organizationId, userId);
    }

    /***----------------------- Forgot Password ---------------------**********/

    public void updateResetPasswordToken(String token, String email) throws UserNotFoundException {
        User user = userRepository.findByEmailAddressAndUserRole(email);

        if (user != null){
            user.setResetPasswordToken(token);
            userRepository.save(user);
        }
        else {
            throw new UserNotFoundException("Could not find user with the email " + email);
        }
    }

    public void updateSetPasswordToken(String token, String email) throws UserNotFoundException {
        User user = userRepository.findByEmployeeEmailAddress(email);

        if (user != null){
            user.setSetPasswordToken(token);
            userRepository.save(user);
        }
        else {
            throw new UserNotFoundException("Could not find user with the email " + email);
        }
    }

    public User getUserByToken(String resetPasswordToken){
        return userRepository.findByResetPasswordToken(resetPasswordToken);
    }


    public User getUserSetPasswordByToken(String passwordToken){
        return userRepository.findBySetPasswordToken(passwordToken);
    }

    public void updatePassword(User user, String newPassword){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(newPassword);

        user.setPassword(encodedPassword);
        user.setResetPasswordToken(null);

        userRepository.save(user);
    }


    public void setFirstTimePassword(User user, String newPassword){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(newPassword);

        user.setPassword(encodedPassword);
        user.setSetPasswordToken(null);

        userRepository.save(user);
    }




    /*UPDATE*/
    public void updateUserRole(int id,
                               String role){

        User user = userRepository.findById(id)
                .orElseThrow(()-> new IllegalStateException("User with the id does not exist"));

        user.setUserRole(role);
    }


    public void updateUserDetails(int userId,
                                  String firstName,
                                  String lastname,
                                  String phoneNumber){
        userRepository.updateUserDetails(userId, firstName, lastname, phoneNumber);
    }

    public void deleteUserWithRoleById(int id){
        userRepository.deleteUserRole(id);
    }


    /*DELETE*/
    public void deleteById(int id) {
        boolean userExists = userRepository.existsById(id);
        if (!userExists){
            throw new IllegalStateException("User by id " + id + " does not exist.");
        }
        userRepository.deleteById(id);
    }

    public void increaseFailedAttempts(User user) {
        int newFailedAttempts = user.getFailedAttempts() + 1;
        userRepository.updateUserFailedAttempts(newFailedAttempts, user.getEmployeeEmailAddress());
    }

    public void lock(User user) {
        user.setAccountNonLocked(false);
        user.setLockTime(new Date());

        userRepository.save(user);
    }

    public boolean unlock(User user){
        long lockTimeInMillis = user.getLockTime().getTime();
        long currentTimeInMillis = System.currentTimeMillis();

        //Duration expired
        if (lockTimeInMillis + LOCK_TIME_DURATION < currentTimeInMillis){
            user.setAccountNonLocked(true);
            user.setLockTime(null);
            user.setFailedAttempts(0);

            userRepository.save(user);
            return true;
        }

        else {
            return false;
        }
    }

    public void resetFailedAttempts(String employeeEmailAddress) {
        userRepository.updateUserFailedAttempts(0, employeeEmailAddress);
    }
}
