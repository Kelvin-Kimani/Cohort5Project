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
    @GetMapping(path = "/login")
    public String getLoginPage(){
        return "login";
    }


    /*Employee Registration*/
    @GetMapping(path = "/register")
    public String getRegistrationForm(Model model) {
        List<Organization> organizations = organizationService.getOrganizations();
        model.addAttribute("organizations", organizations);
        model.addAttribute("user", new User());
        return "user_registration";
    }

    @PostMapping(value = "/register")
    public String addNewUser(User user) {
        userService.createUser(user);
        return "redirect:/";
    }

    /*Forgot Password*/
    @GetMapping(path = "/forgot_password")
    public String getForgotPassword(){
        return "forgot_password";
    }

}