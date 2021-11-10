package com.tracom.cohort5project.Controllers;

import com.tracom.cohort5project.Entities.User;
import com.tracom.cohort5project.Security.CustomUserDetails;
import com.tracom.cohort5project.Services.MeetingService;
import com.tracom.cohort5project.Services.OrganizationService;
import com.tracom.cohort5project.Services.RoomService;
import com.tracom.cohort5project.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = "/officer")
public class OOController {

    private OrganizationService organizationService;
    private UserService userService;
    private RoomService roomService;
    private MeetingService meetingService;

    @Autowired
    public OOController(OrganizationService organizationService, UserService userService, RoomService roomService, MeetingService meetingService) {
        this.organizationService = organizationService;
        this.userService = userService;
        this.roomService = roomService;
        this.meetingService = meetingService;
    }

    @GetMapping(path = "/dashboard")
    public String getOfficerWelcomePage(){
        return "officer/oo_welcome";
    }

    /*USER*/
    @GetMapping(path = "/profile")
    public String getUserProfile(@AuthenticationPrincipal CustomUserDetails loggedUser, Model model){
        String email = loggedUser.getUsername();
        User user = userService.getUserByEmail(email);

        model.addAttribute("loggedUser", user);
        return "officer/user_profile";
    }

    @GetMapping(path = "/edit_profile")
    public String getEditProfile(@AuthenticationPrincipal CustomUserDetails loggedUser, Model model){
        String email = loggedUser.getUsername();
        User user = userService.getUserByEmail(email);

        model.addAttribute("editUser", user);

        return "officer/edit_profile";
    }

    /*MEETINGS*/
    @GetMapping(path = "/meetings")
    public String getMeetings(){
        return "officer/view_meetings";
    }

    @GetMapping(path = "/create_meeting")
    public String getCreateMeetings(){
        return "officer/create_meeting";
    }

}
