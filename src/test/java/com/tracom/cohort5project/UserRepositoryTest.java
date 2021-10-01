package com.tracom.cohort5project;

import com.tracom.cohort5project.Entities.User;
import com.tracom.cohort5project.Repositories.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    public void testCreateUser(){
        User user = new User();
        user.setUserId(1);
        user.setEmployeeName("Jeff Njoroge");
        user.setUserRole("Organization Officer");
        user.setEmployeeDepartment("Software Development");
        user.setEmployeeEmailAddress("jj.njoro@tracom.co.ke");
        user.setEmployeeGender("MALE");
        user.setEmployeePhoneNumber("+254790838747");
        user.setPassword("JayJay");

        User savedUser = userRepository.save(user);

        User existsUser = testEntityManager.find(User.class, savedUser.getUserId());

        Assertions.assertThat(existsUser.getEmployeeEmailAddress().equals(savedUser.getEmployeeEmailAddress()));
    }

    @Test
    public void testFindUserByEmail(){
        String email = "kelvin.kimani@tracom.co.ke";

        User user = userRepository.findByEmployeeEmailAddress(email);
        
        Assertions.assertThat(user).isNotNull();
    }
}
