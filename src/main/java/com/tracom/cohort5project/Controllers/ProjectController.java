package com.tracom.cohort5project.Controllers;

import com.tracom.cohort5project.Entities.*;
import com.tracom.cohort5project.Services.OrganizationService;
import com.tracom.cohort5project.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * In the beginning, there was data, and it was good. But then people wanted to access the data through various means.
 */

@Controller
public class ProjectController {

    private OrganizationService organizationService;
    private UserService userService;

    @Autowired
    public ProjectController(OrganizationService organizationService, UserService userService) {
        this.organizationService = organizationService;
        this.userService = userService;
    }

    /*Home Page*/
    @GetMapping("")
    public String viewHomePage() {
        return "welcome";
    }


    /*Login*/
    @GetMapping("/login")
    public String getLoginPage(){
        return "login";
    }



    /*Employee Registration*/
    @GetMapping("/register")
    public String getRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "user_registration";
    }

    @PostMapping("/user_registration")
    public String addNewUser(User user) {
        //Done by the user
        userService.createUser(user);
        return "welcome";
    }


    /*User*/
    @GetMapping(path = "/registered_users")
    public String showRegisteredUsers(Model model){
        List<User> registeredUsers = userService.getUsers();
        model.addAttribute("registeredUsersList", registeredUsers);
        return "list_registered_users";
    }

    @RequestMapping("/edit_user/{id}")
    public ModelAndView showCreateUser(@PathVariable(name = "id") int id){

        ModelAndView mnv = new ModelAndView("create_user");

        //User object
        User user = userService.getUserById(id);
        mnv.addObject("createUser", user);

        return mnv;
    }

    @PostMapping(path = "/change_user_role/{userId}")
    public String createUser(@PathVariable(name = "userId") int userId,
                             @RequestParam(required = false, name = "userRole") String role){
        userService.createSystemUserById(userId, role);
        return "list_registered_users";
    }

}