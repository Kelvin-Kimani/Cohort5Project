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
import org.springframework.web.bind.annotation.*;
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
    public String homeAdminDashboard(@AuthenticationPrincipal CustomUserDetails loggedUser, Model model){

        String email = loggedUser.getUsername();
        User user = userService.getUserByEmail(email);

        int organizationId = user.getOrganization().getOrganizationId();

        int users = userService.numberOfUsersWithRolesAndByOrganization(organizationId);
        int rooms = roomService.noOfRoomsInOrganization(organizationId);

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
    public String getRooms(@AuthenticationPrincipal CustomUserDetails loggedUser, Model model){

        String email = loggedUser.getUsername();
        User user = userService.getUserByEmail(email);

        int organizationId = user.getOrganization().getOrganizationId();
        //List<Room> roomsList = roomService.showRooms();

        List<Room> roomsList = roomService.showRoomsInOrganization(organizationId);
        model.addAttribute("roomsList", roomsList);
        return "admin/view_rooms";
    }
    @GetMapping(path = "/create_room")
    public String getCreateRoomForm(@AuthenticationPrincipal CustomUserDetails loggedUser, Model model){
        model.addAttribute("room", new Room());

        String email = loggedUser.getUsername();
        User user = userService.getUserByEmail(email);

        model.addAttribute("loggedUser", user);

        return "admin/create_boardroom";
    }

    @PostMapping(value = "/create_room")
    public String createRoom(Room room){
        roomService.createRoom(room);
        return "redirect:/admin/rooms";
    }

    @RequestMapping("/delete_room/{roomId}")
    public String deleteRoom(@PathVariable(name = "roomId") int id){
        roomService.deleteRoom(id);
        return "redirect:/admin/rooms";
    }

    /*User*/
    @GetMapping(path = "/create_user")
    public String showCreateUser(@AuthenticationPrincipal CustomUserDetails loggedUser, Model model){

        //Users without roles and organization
        String email = loggedUser.getUsername();
        User user = userService.getUserByEmail(email);

        int organizationId = user.getOrganization().getOrganizationId();

        //List<User> registeredUsers = userService.getUsersWithoutRoles();
        List<User> registeredUsers = userService.getUsersWithoutRolesAndByOrganization(organizationId);
        model.addAttribute("registeredUsersList", registeredUsers);
        model.addAttribute("editUserRole", new User());
        return "admin/create_user";
    }

    @RequestMapping("/edit_user_role")
    public String createUser(@RequestParam(value = "userId") int userId,
                             @RequestParam(value = "userRole") String userRole){
        userService.createSystemUserById(userId, userRole);
        return "redirect:/admin/create_user";
    }

    @GetMapping("/users")
    public String getUsers(@AuthenticationPrincipal CustomUserDetails loggedUser, Model model){

        //Users with roles and organization
        String email = loggedUser.getUsername();
        User user = userService.getUserByEmail(email);

        int organizationId = user.getOrganization().getOrganizationId();
        //Users with roles
        //List<User> usersList = userService.getUsersWithRoles();
        List<User> usersList = userService.getUsersWithRolesAndByOrganization(organizationId);
        model.addAttribute("loggedUser", user);
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
    public String createMeeting(@AuthenticationPrincipal CustomUserDetails loggedUser, Model model){

        String email = loggedUser.getUsername();
        User user = userService.getUserByEmail(email);

        int organizationId = user.getOrganization().getOrganizationId();

        List<Room> roomsList = roomService.showRoomsInOrganization(organizationId);
        model.addAttribute("roomsList", roomsList);
        model.addAttribute("loggedUser", user);
        model.addAttribute("meeting", new Meeting());
        return "admin/create_meeting";
    }

    @PostMapping(path = "/create_meeting")
    public String createMeeting(Meeting meeting){
        meetingService.createMeeting(meeting);
        return "redirect:/admin/meetings";
    }

}
