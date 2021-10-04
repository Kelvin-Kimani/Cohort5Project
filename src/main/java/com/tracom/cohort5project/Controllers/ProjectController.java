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

    @GetMapping("")
    public String viewHomePage() {
        return "welcome";
    }

    @GetMapping("/dashboard")
    public String viewdashboard() {
        return "admin_dashboard";
    }


    /*Employee*/
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

    @GetMapping("/users")
    public String getUsers(Model model){
        //Users with roles
        List<User> usersList = userService.getUsersWithRoles();
        model.addAttribute("userDetails", usersList);
        return "list_users";
    }

    @RequestMapping("/delete_user/{id}")
    public String deleteUser(@PathVariable(name = "id") int id){
        userService.deleteById(id);
        return "list_users";
    }




    /*Organization*/
    @GetMapping("/organization")
    public String getOrganizationForm(Model model) {
        model.addAttribute("organization", new Organization());
        return "add_organization";
    }

    @PostMapping("/add_organization")
    public String createOrganization(Organization organization) {
        organizationService.createOrganization(organization);
        return "welcome";
    }



    /*Login*/
    @GetMapping("/login")
    public String getLoginPage(Model model){
        return "user_login";
    }

    @GetMapping("/adminWelcomePage")
    public String getAdminDashboard(){
        return "admin_welcome_page";
    }

}