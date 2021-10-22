package com.tracom.cohort5project.Controllers;

import com.tracom.cohort5project.Entities.Organization;
import com.tracom.cohort5project.Entities.Room;
import com.tracom.cohort5project.Entities.User;
import com.tracom.cohort5project.Security.CustomUserDetails;
import com.tracom.cohort5project.Services.OrganizationService;
import com.tracom.cohort5project.Services.RoomService;
import com.tracom.cohort5project.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping(path = "/admin")
public class AdminController {

    private OrganizationService organizationService;
    private UserService userService;
    private RoomService roomService;

    @Autowired
    public AdminController(OrganizationService organizationService, UserService userService, RoomService roomService) {
        this.organizationService = organizationService;
        this.userService = userService;
        this.roomService = roomService;
    }

    /*Dashboard*/
    @GetMapping(path = "/dashboard")
    public String homeAdminDashboard(){
        return "admin_welcome_page";
    }

    @GetMapping(path = "/profile")
    public String getUserProfile(@AuthenticationPrincipal CustomUserDetails loggedUser, Model model){
        String email = loggedUser.getUsername();
        User user = userService.getUserByEmail(email);

        model.addAttribute("loggedUser", user);
        return "user_profile";
    }

    @PostMapping(path = "/profile/update")
    public String updateUserProfile(User user, RedirectAttributes redirectAttributes, @AuthenticationPrincipal CustomUserDetails loggedUser){

        userService.createUser(user);

        loggedUser.setFullName(user.getEmployeeFirstName() , user. getEmployeeLastName());

        redirectAttributes.addFlashAttribute("message", "Profile Updated successfully");
        return "redirect:/admin/user_profile";
    }



    @GetMapping("/users")
    public String getUsers(Model model){
        //Users with roles
        List<User> usersList = userService.getUsersWithRoles();
        model.addAttribute("userDetails", usersList);
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

    /*Board Room*/
    @GetMapping(path = "/rooms")
    public String getRooms(Model model){

        List<Room> roomsList = roomService.showRooms();
        model.addAttribute("roomsList", roomsList);
        return "view_rooms";
    }
    @GetMapping(path = "/create_room")
    public String getCreateRoomForm(Model model){

        model.addAttribute("room", new Room());
        return "create_boardroom";
    }

    @PostMapping(path = "/create_room")
    public String createRoom(Room room){
        roomService.createRoom(room);
        return "create_boardroom";
    }

}
