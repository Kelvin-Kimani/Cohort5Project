package com.tracom.cohort5project.Controllers;

import com.tracom.cohort5project.Entities.Meeting;
import com.tracom.cohort5project.Entities.Organization;
import com.tracom.cohort5project.Entities.Room;
import com.tracom.cohort5project.Entities.User;
import com.tracom.cohort5project.Exceptions.UserNotFoundException;
import com.tracom.cohort5project.Security.CustomUserDetails;
import com.tracom.cohort5project.Services.MeetingService;
import com.tracom.cohort5project.Services.OrganizationService;
import com.tracom.cohort5project.Services.RoomService;
import com.tracom.cohort5project.Services.UserService;
import com.tracom.cohort5project.Utilities.Utility;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.List;

@Controller
@RequestMapping(path = "/admin")
public class AdminController {

    private OrganizationService organizationService;
    private UserService userService;
    private RoomService roomService;
    private MeetingService meetingService;
    private JavaMailSender mailSender;

    @Autowired
    public AdminController(OrganizationService organizationService, UserService userService, RoomService roomService, MeetingService meetingService, JavaMailSender mailSender) {
        this.organizationService = organizationService;
        this.userService = userService;
        this.roomService = roomService;
        this.meetingService = meetingService;
        this.mailSender = mailSender;
    }

    /*Dashboard*/
    @GetMapping(path = "/dashboard")
    public String homeAdminDashboard(@AuthenticationPrincipal CustomUserDetails loggedUser, Model model){

        String email = loggedUser.getUsername();
        User user = userService.getUserByEmail(email);

        int organizationId = user.getOrganization().getOrganizationId();

        int users = userService.numberOfUsersWithRolesAndByOrganization(organizationId);
        int rooms = roomService.noOfRoomsInOrganization(organizationId);
        int meetingsToBeAttended = meetingService.numberOfMeetingsToBeAttendedByOrganization(organizationId);
        int meetingsAttended = meetingService.numberOfMeetingsAttendedByOrganization(organizationId);
        List<Meeting> meetings = meetingService.getOrganizationMeetingsForLaterDate(organizationId);
        List<Meeting> todayMeetings = meetingService.getOrganizationMeetingsToday(organizationId);

        model.addAttribute("noOfMeetingsToBeAttended", meetingsToBeAttended);
        model.addAttribute("noOfMeetingsAttended", meetingsAttended);
        model.addAttribute("noOfUsers", users);
        model.addAttribute("noOfRooms", rooms);
        model.addAttribute("meetings", meetings);
        model.addAttribute("todayMeetings", todayMeetings);
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
    public String updateUserProfile(@AuthenticationPrincipal CustomUserDetails loggedUser,
                                    @RequestParam(value = "userId", required = false) int userId,
                                    @RequestParam(value = "firstName", required = false) String firstName,
                                    @RequestParam(value = "lastName", required = false) String lastName,
                                    @RequestParam(value = "phone", required = false) String phoneNumber,
                                    RedirectAttributes redirectAttributes){

        //Perform update
        userService.updateUserDetails(userId, firstName, lastName, phoneNumber);

        //Set current loggedIn user details on top.
        loggedUser.setFirstName(firstName);
        loggedUser.setLastName(lastName);


        redirectAttributes.addFlashAttribute("message", "Profile Updated successfully");
        return "redirect:/admin/profile";
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

    @GetMapping(path = "/edit_room/{roomId}")
    public ModelAndView getEditRoomForm(@AuthenticationPrincipal CustomUserDetails loggedUser,  @PathVariable(name = "roomId") int roomId){

        ModelAndView mvn = new ModelAndView("admin/edit_boardroom");

        String email = loggedUser.getUsername();
        User user = userService.getUserByEmail(email);
        Room room = roomService.getRoom(roomId);

        mvn.addObject("editRoom", room);
        mvn.addObject("loggedUser", user);
        return mvn;
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
    @GetMapping("/users")
    public String getUsers(@AuthenticationPrincipal CustomUserDetails loggedUser, Model model){

        //Users with roles and organization
        String email = loggedUser.getUsername();
        User user = userService.getUserByEmail(email);

        int organizationId = user.getOrganization().getOrganizationId();
        //Users with roles
        List<User> usersList = userService.getUsersWithRolesAndByOrganization(organizationId);
        model.addAttribute("loggedUser", user);
        model.addAttribute("userDetails", usersList);
        return "admin/list_users";
    }

    @GetMapping(path = "/create_user")
    public String showCreateUser(@AuthenticationPrincipal CustomUserDetails loggedUser, Model model){

        //Users without roles and organization
        String email = loggedUser.getUsername();
        User user = userService.getUserByEmail(email);

        int organizationId = user.getOrganization().getOrganizationId();

        //List<User> registeredUsers = userService.getUsersWithoutRoles();
        List<User> registeredUsers = userService.getUsersWithoutRolesAndByOrganization(organizationId);
        model.addAttribute("registeredUsersList", registeredUsers);
        model.addAttribute("addUserRole", new User());
        return "admin/create_user";
    }

    @RequestMapping("/add_user_role")
    public String createUser(@RequestParam(value = "userId") int userId,
                             @RequestParam(value = "userRole") String userRole,
                             HttpServletRequest request,
                             Model model){

        userService.createSystemUserById(userId, userRole);

        User user = userService.getUserById(userId);

        String email = user.getEmployeeEmailAddress();
        String token = RandomString.make(45);
        String fullNames = user.getEmployeeFirstName() + " " + user.getEmployeeLastName();
        System.out.println(email + " : " + token);


        try {

            userService.updateSetPasswordToken(token, email);

            //Generate Password Link
            String passwordLink = Utility.getSiteURL(request) + "/set_password?token=" + token;

            System.out.println(passwordLink);

            //send email
            sendEmail(fullNames, email, passwordLink);

            model.addAttribute("message", "User created and email sent for them to set their password.");

        }
        catch (UserNotFoundException | MessagingException | UnsupportedEncodingException ex){
            model.addAttribute("error", ex.getMessage());
        }

        return "redirect:/admin/create_user";
    }

    private void sendEmail(String fullNames, String email, String passwordLink) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom("staff@mop.com", "Meeting Office Planner");
        helper.setTo(email);

        String subject = "Welcome To Meeting Office Planner";

        String content = "<p>Hi " + fullNames + ",</p>"
                + "<p>Welcome to Meeting Office Planner, where we help you schedule your meetings in an efficient manner.</p>"
                + "<p>Provided is a link that lets you set your password for the first time before accessing our services.</p>"
                + "<p><b><a href=\"" + passwordLink + "\">Set password</a></b></p>"
                + "<p>Once again, Welcome!</p><br>"
                + "<p><b>Regards,</b></p>"
                + "<p><b>Meeting Office Planner</b></p>";

        helper.setSubject(subject);
        helper.setText(content, true);

        mailSender.send(message);
    }

    @GetMapping(path = "/edit_user_role/{userId}")
    public ModelAndView getEditUserRole(@PathVariable(name = "userId") int userId){

        ModelAndView mvn = new ModelAndView("admin/edit_user_role");

        User user = userService.getUserById(userId);

        mvn.addObject("editUserRole", user);
        return mvn;
    }

    @RequestMapping("/edit_user_role")
    public String updateUserRole(@RequestParam(value = "userId") int userId,
                             @RequestParam(value = "userRole") String userRole){
        userService.createSystemUserById(userId, userRole);
        return "redirect:/admin/users";
    }

    @GetMapping(path = "/delete_user_role/{userId}")
    public String deleteUserWithRole(@PathVariable(name = "userId") int userId){
        userService.deleteUserWithRoleById(userId);
        return "redirect:/admin/users";
    }

    /** ----- Meeting -----**/
    @GetMapping(path = "/meetings")
    public String getMeetings(@AuthenticationPrincipal CustomUserDetails loggedUser, Model model){
        String email = loggedUser.getUsername();
        User user = userService.getUserByEmail(email);

        int organizationId = user.getOrganization().getOrganizationId();
        List<Meeting> upcomingMeetings = meetingService.getOrganizationMeetingsForLaterDate(organizationId);
        List<Meeting> pastMeetings = meetingService.getOrganizationMeetingsForPastDate(organizationId);


        model.addAttribute("pastMeetings", pastMeetings);
        model.addAttribute("upcomingMeetings", upcomingMeetings);
        return "admin/view_meetings";
    }

    @GetMapping(path = "/upcoming_meetings")
    public String getUpcomingMeetings(@AuthenticationPrincipal CustomUserDetails loggedUser, Model model){
        String email = loggedUser.getUsername();
        User user = userService.getUserByEmail(email);

        int organizationId = user.getOrganization().getOrganizationId();
        List<Meeting> upcomingMeetings = meetingService.getOrganizationMeetingsForLaterDate(organizationId);

        model.addAttribute("upcomingMeetings", upcomingMeetings);
        return "admin/upcoming_meetings";
    }


    @GetMapping(path = "/past_meetings")
    public String getPastMeetings(@AuthenticationPrincipal CustomUserDetails loggedUser, Model model){
        String email = loggedUser.getUsername();
        User user = userService.getUserByEmail(email);

        int organizationId = user.getOrganization().getOrganizationId();
        List<Meeting> pastMeetings = meetingService.getOrganizationMeetingsForPastDate(organizationId);

        model.addAttribute("pastMeetings", pastMeetings);
        return "admin/past_meetings";
    }

    @GetMapping(path = "/create_meeting")
    public String createMeeting(@AuthenticationPrincipal CustomUserDetails loggedUser, Model model){

        String email = loggedUser.getUsername();
        User user = userService.getUserByEmail(email);

        int organizationId = user.getOrganization().getOrganizationId();
        int userId = user.getUserId();

        List<Room> roomsList = roomService.showRoomsInOrganization(organizationId);
        List<User> users = userService.getEligibleCoOwners(organizationId, userId);

        model.addAttribute("coOwnersEligible", users);
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

    @GetMapping(path = "/add_coowners/{meetingId}")
    public String addMeetingCoOwners(@AuthenticationPrincipal CustomUserDetails loggedUser, @PathVariable(name = "meetingId") int meetingId, Model model){

        String email = loggedUser.getUsername();
        User user = userService.getUserByEmail(email);

        int organizationId = user.getOrganization().getOrganizationId();
        int userId = user.getUserId();

        Meeting meeting = meetingService.getMeeting(meetingId);
        List<User> users = userService.getEligibleCoOwners(organizationId, userId);

        model.addAttribute("meeting", meeting);
        model.addAttribute("users", users);
        return "admin/add_coowners";
    }

    @PostMapping(path = "/add_coowners")
    public String addCoOwners( @RequestParam(value = "meetingId", required = false) int meetingId,
                               @RequestParam(value = "users", required = false) List<User> users){

        Meeting meeting = meetingService.getMeeting(meetingId);
        meetingService.updateCoOwners(meeting, users);

        return "redirect:/admin/meetings";

    }

    @GetMapping("/meeting{meetingId}")
    public ModelAndView getMeetingDetails(@AuthenticationPrincipal CustomUserDetails loggedUser, @PathVariable(name = "meetingId") int meetingId){

        //Get current loggedin user
        String email = loggedUser.getUsername();
        User user = userService.getUserByEmail(email);

        ModelAndView mvn = new ModelAndView("admin/meeting_details");

        Meeting meeting = meetingService.getMeeting(meetingId);
        mvn.addObject("meeting", meeting);
        mvn.addObject("loggedUser", user);
        return mvn;
    }

    @GetMapping(path = "/edit_meeting/{meetingId}")
    public ModelAndView getEditMeetingForm(@AuthenticationPrincipal CustomUserDetails loggedUser, @PathVariable(name = "meetingId") int meetingId){

        ModelAndView mvn = new ModelAndView("admin/edit_meeting");

        String email = loggedUser.getUsername();
        User user = userService.getUserByEmail(email);

        int organizationId = user.getOrganization().getOrganizationId();

        List<Room> roomsList = roomService.showRoomsInOrganization(organizationId);
        Meeting meeting = meetingService.getMeeting(meetingId);

        mvn.addObject("loggedUser", user);
        mvn.addObject("editMeeting", meeting);
        mvn.addObject("rooms", roomsList);

        return mvn;
    }

    @RequestMapping("/delete_meeting/{meetingId}")
    public String deleteMeeting(@PathVariable(name = "meetingId") int meetingId){
        meetingService.deleteMeetingById(meetingId);
        return "redirect:/admin/meetings";
    }

}
