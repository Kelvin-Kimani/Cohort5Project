package com.tracom.cohort5project.Services;

import com.tracom.cohort5project.Entities.User;
import com.tracom.cohort5project.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService {

    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    /*
    CREATE
    Implementing HTTP POST
    */
    public void createUser(User user){
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

    public void setPassword(int id, String password){
    }

    /*READ*/
    public List<User> getUsers(){
        return userRepository.findAll();
    }
    public List<User> getUsersWithRoles() {
        return userRepository.findAllWithRoles();
    }

    public User getUserById(int id) {
        return userRepository.findById(id)
                .orElseThrow(()-> new IllegalStateException("User with id " + id + " does not exist."));
    }


    /*UPDATE*/
    public void updateUser(int id,
                               String role){

        User user = userRepository.findById(id)
                .orElseThrow(()-> new IllegalStateException("User with the id does not exist"));

        user.setUserRole(role);
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

}
