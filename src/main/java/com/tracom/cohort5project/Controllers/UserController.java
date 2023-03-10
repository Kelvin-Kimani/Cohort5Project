package com.tracom.cohort5project.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = "/user")
public class UserController {

    @GetMapping(path = "/dashboard")
    public String getUserWelcomePage() {
        return "user/user_welcome";
    }
}
