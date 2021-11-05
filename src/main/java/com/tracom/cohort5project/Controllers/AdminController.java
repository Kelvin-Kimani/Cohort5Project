package com.tracom.cohort5project.Controllers;

import com.tracom.cohort5project.Entities.Meeting;
import com.tracom.cohort5project.Entities.Organization;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping(path = "/admin")
public class AdminController {

    private OrganizationService organizationService;
    private UserService userService;
    private RoomService roomService;
    private MeetingService meetingService;

    @Autowired
    public AdminController(OrganizationService organizationService, UserService userService, RoomService roomService, MeetingService meetingService) {
        this.organizationService = organizationService;
        this.userService = userService;
        this.roomService = roomService;
        this.meetingService = meetingService;
    }

    /*Dashboard*/
    @GetMapping(path = "/dashboard")
    public String homeAdminDashboard(Model model){
        int users = userService.numberOfUsersWithRoles();
        int rooms = roomService.noOfRooms();

        model.addAttribute("noOfUsers", users);
        model.addAttribute("noOfRooms", rooms);

        return "admin/admin_welcome_page";
    }

    @GetMapping(path = "/profile")
    public String getUserProfile(@AuthenticationPrincipal CustomUserDetails loggedUser, Model model){
        String email = loggedUser.getUsername();
        User user = userService.getUserByEmail(email);

        model.addAttribute("loggedUser", user);
        return "admin/user_profile";
    }

    @GetMapping("/edit_profile")
    public String getUserProfileDetails(@AuthenticationPrincipal CustomUserDetails loggedUser, Model model){
        String email = loggedUser.getUsername();
        User user = userService.getUserByEmail(email);

        model.addAttribute("editUser", user);

        return "admin/edit_profile";
    }

    @PostMapping(path = "/edit_profile/update")
    public String updateUserProfile(User user, RedirectAttributes redirectAttributes, @AuthenticationPrincipal CustomUserDetails loggedUser){
        userService.createUser(user);

        loggedUser.setFullName(user.getEmployeeFirstName() , user. getEmployeeLastName());

        redirectAttributes.addFlashAttribute("message", "Profile Updated successfully");
        return "redirect:/admin/user_profile";
    }


    /*Organization*/
    @GetMapping(path = "/organization")
    public String getOrganizationForm(Model model) {
        model.addAttribute("organization", new Organization());
        return "admin/add_organization";
    }

    @PostMapping(path = "/add_organization")
    public String createOrganization(Organization organization) {
        organizationService.createOrganization(organization);
        return "redirect:/admin/organization";
    }

    /*Board Room*/
    @GetMapping(path = "/rooms")
    public String getRooms(Model model){

        List<Room> roomsList = roomService.showRooms();
        model.addAttribute("roomsList", roomsList);
        return "admin/view_rooms";
    }
    @GetMapping(path = "/create_room")
    public String getCreateRoomForm(Model model){

        model.addAttribute("room", new Room());
        return "admin/create_boardroom";
    }

    @PostMapping(path = "/create_room")
    public String createRoom(Room room){
        roomService.createRoom(room);
        return "admin/create_boardroom";
    }

    @RequestMapping("/delete_room/{roomId}")
    public String deleteRoom(@PathVariable(name = "roomId") int id){
        roomService.deleteRoom(id);
        return "redirect:/admin/rooms";
    }

    /*User*/
    @GetMapping(path = "/registered_users")
    public String showRegisteredUsers(Model model){
        List<User> registeredUsers = userService.getUsersWithoutRoles();
        model.addAttribute("registeredUsersList", registeredUsers);
        return "admin/list_registered_users";
    }

    @RequestMapping("/edit_user/{id}")
    public ModelAndView showCreateUser(@PathVariable(name = "id") int id){

        ModelAndView mnv = new ModelAndView("admin/create_user");

        //User object
        User user = userService.getUserById(id);
        mnv.addObject("createUser", user);

        return mnv;
    }

    @GetMapping("/users")
    public String getUsers(Model model){
        //Users with roles
        List<User> usersList = userService.getUsersWithRoles();
        model.addAttribute("userDetails", usersList);
        return "admin/list_users";
    }


    @GetMapping(path = "/delete_user_role/{userId}")
    public String deleteUserWithRole(@PathVariable(name = "userId") int userId){
        userService.deleteUserWithRoleById(userId);
        return "redirect:/admin/users";
    }

    /** ----- Meeting -----**/
    @GetMapping(path = "/meetings")
    public String getMeetings(){
        return "admin/view_meetings";
    }

    @GetMapping(path = "/create_meeting")
    public String createMeeting(Model model){

        List<Room> roomsList = roomService.showRooms();
        model.addAttribute("roomsList", roomsList);
        model.addAttribute("meeting", new Meeting());
        return "admin/create_meeting";
    }

    @PostMapping(path = "/create_meeting")
    public String createMeeting(Meeting meeting){
        meetingService.createMeeting(meeting);
        return "redirect:/admin/meetings";
    }

}
