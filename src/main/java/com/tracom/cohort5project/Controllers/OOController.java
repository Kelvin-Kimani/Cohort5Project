package com.tracom.cohort5project.Controllers;

import com.tracom.cohort5project.Entities.Meeting;
import com.tracom.cohort5project.Entities.Room;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

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

    public User getLoggedUser(@AuthenticationPrincipal CustomUserDetails loggedUser){
        String email = loggedUser.getUsername();
        User user = userService.getUserByEmail(email);

        return user;
    }

    @GetMapping(path = "/dashboard")
    public String getOfficerWelcomePage(@AuthenticationPrincipal CustomUserDetails loggedUser, Model model){

        int organizationId = getLoggedUser(loggedUser).getOrganization().getOrganizationId();
        int meetingsToBeAttended = meetingService.numberOfMeetingsToBeAttendedByOrganization(organizationId);
        int meetingsAttended = meetingService.numberOfMeetingsAttendedByOrganization(organizationId);
        List<Meeting> meetings = meetingService.getOrganizationMeetingsForLaterDate(organizationId);
        List<Meeting> todayMeetings = meetingService.getOrganizationMeetingsToday(organizationId);

        model.addAttribute("noOfMeetingsToBeAttended", meetingsToBeAttended);
        model.addAttribute("noOfMeetingsAttended", meetingsAttended);
        model.addAttribute("meetings", meetings);
        model.addAttribute("todayMeetings", todayMeetings);

        return "officer/oo_welcome";
    }

    /*USER*/
    @GetMapping(path = "/profile")
    public String getUserProfile(@AuthenticationPrincipal CustomUserDetails loggedUser, Model model){
        model.addAttribute("loggedUser", getLoggedUser(loggedUser));
        return "officer/user_profile";
    }

    @GetMapping(path = "/edit_profile")
    public String getEditProfile(@AuthenticationPrincipal CustomUserDetails loggedUser, Model model){
        model.addAttribute("editUser", getLoggedUser(loggedUser));

        return "officer/edit_profile";
    }

    /*MEETINGS*/
    @GetMapping(path = "/meetings")
    public String getMeetings(@AuthenticationPrincipal CustomUserDetails loggedUser, Model model){

        int organizationId = getLoggedUser(loggedUser).getOrganization().getOrganizationId();
        List<Meeting> upcomingMeetings = meetingService.getOrganizationMeetingsForLaterDate(organizationId);
        List<Meeting> pastMeetings = meetingService.getOrganizationMeetingsForPastDate(organizationId);

        model.addAttribute("pastMeetings", pastMeetings);
        model.addAttribute("upcomingMeetings", upcomingMeetings);
        return "officer/view_meetings";
    }

    @GetMapping(path = "/create_meeting")
    public String getCreateMeetings(@AuthenticationPrincipal CustomUserDetails loggedUser, Model model){

        int organizationId = getLoggedUser(loggedUser).getOrganization().getOrganizationId();
        int userId = getLoggedUser(loggedUser).getUserId();

        List<Room> roomsList = roomService.showRoomsInOrganization(organizationId);
        List<User> users = userService.getEligibleCoOwners(organizationId, userId);

        model.addAttribute("coOwnersEligible", users);
        model.addAttribute("roomsList", roomsList);
        model.addAttribute("loggedUser", getLoggedUser(loggedUser));
        model.addAttribute("meeting", new Meeting());

        return "officer/create_meeting";
    }

    @PostMapping(path = "/create_meeting")
    public String createMeeting(Meeting meeting){
        meetingService.createMeeting(meeting);
        return "redirect:/officer/meetings";
    }

    @GetMapping(path = "/add_coowners/{meetingId}")
    public String addMeetingCoOwners(@AuthenticationPrincipal CustomUserDetails loggedUser, @PathVariable(name = "meetingId") int meetingId, Model model){

        int organizationId = getLoggedUser(loggedUser).getOrganization().getOrganizationId();
        int userId = getLoggedUser(loggedUser).getUserId();

        System.out.println(userId);

        Meeting meeting = meetingService.getMeeting(meetingId);
        List<User> users = userService.getEligibleCoOwners(organizationId, userId);

        model.addAttribute("meeting", meeting);
        model.addAttribute("users", users);
        return "officer/add_coowners";
    }

    @PostMapping(path = "/add_coowners")
    public String addCoOwners(@RequestParam(value = "meetingId", required = false) int meetingId,
                              @RequestParam(value = "users", required = false) List<User> users){

        Meeting meeting = meetingService.getMeeting(meetingId);
        meetingService.updateCoOwners(meeting, users);

        return "redirect:/officer/meetings";
    }


    @GetMapping("/meeting{meetingId}")
    public ModelAndView getMeetingDetails(@AuthenticationPrincipal CustomUserDetails loggedUser, @PathVariable(name = "meetingId") int meetingId){

        ModelAndView mvn = new ModelAndView("officer/meeting_details");

        Meeting meeting = meetingService.getMeeting(meetingId);
        mvn.addObject("meeting", meeting);
        mvn.addObject("loggedUser", getLoggedUser(loggedUser));
        return mvn;
    }


    @GetMapping(path = "/edit_meeting/{meetingId}")
    public ModelAndView getEditMeetingForm(@AuthenticationPrincipal CustomUserDetails loggedUser, @PathVariable(name = "meetingId") int meetingId){

        ModelAndView mvn = new ModelAndView("officer/edit_meeting");

        int organizationId = getLoggedUser(loggedUser).getOrganization().getOrganizationId();

        List<Room> roomsList = roomService.showRoomsInOrganization(organizationId);
        Meeting meeting = meetingService.getMeeting(meetingId);

        mvn.addObject("loggedUser", getLoggedUser(loggedUser));
        mvn.addObject("editMeeting", meeting);
        mvn.addObject("rooms", roomsList);

        return mvn;
    }


    @RequestMapping("/delete_meeting/{meetingId}")
    public String deleteMeeting(@PathVariable(name = "meetingId") int meetingId){
        meetingService.deleteMeetingById(meetingId);
        return "redirect:/officer/meetings";
    }



}
