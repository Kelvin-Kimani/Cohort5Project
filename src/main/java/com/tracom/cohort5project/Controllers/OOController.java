package com.tracom.cohort5project.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = "/officer")
public class OOController {

    @GetMapping(path = "/dashboard")
    public String getOfficerWelcomePage(){
        return "organization_officer/oo_welcome";
    }
}
