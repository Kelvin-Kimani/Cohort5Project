package com.tracom.cohort5project.Controllers;

import com.tracom.cohort5project.Entities.*;
import com.tracom.cohort5project.Services.OrganizationService;
import com.tracom.cohort5project.Exceptions.UserNotFoundException;
import com.tracom.cohort5project.Services.UserService;
import com.tracom.cohort5project.Utilities.Utility;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * In the beginning, there was data, and it was good. But then people wanted to access the data through various means.
 */

@Controller
public class ProjectController {

    private OrganizationService organizationService;
    private UserService userService;
    private JavaMailSender mailSender;

    @Autowired
    public ProjectController(OrganizationService organizationService, UserService userService, JavaMailSender mailSender) {
        this.organizationService = organizationService;
        this.userService = userService;
        this.mailSender = mailSender;
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
    public String getForgotPassword(Model model) {
        return "forgot_password";
    }

    @PostMapping("/forgot_password")
    public String processForgotPasswordForm(HttpServletRequest request, Model model){
        String email = request.getParameter("email");
        String token = RandomString.make(45);

        try {
            userService.updateResetPasswordToken(token, email);

            //generate reset pwd link
            String resetPasswordLink = Utility.getSiteURL(request) + "/reset_password?token=" + token;


            sendEmail(email, resetPasswordLink);
            //send email
            model.addAttribute("message", "We have sent a link to reset your password to the email.");

        } catch (UserNotFoundException | MessagingException | UnsupportedEncodingException ex) {
            model.addAttribute("error", ex.getMessage());
        }
        return "forgot_password";
    }

    private void sendEmail(String email, String resetPasswordLink) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom("contact@shopme.com", "Meeting Office Planner");
        helper.setTo(email);

        String subject = "Password Reset - Meeting Office Planner";

        String content = "<p>Hi,</p>"
                + "<p>Here is your requested reset password link</p>"
                + "<p>Click and follow instructions to reset password</p>"
                + "<p><b><a href=\"" + resetPasswordLink + "\">Change my password</a></b></p>"
                + "<p>Kindly ignore the email if you didn't request for a password reset.</p>"
                + "<p>Regards,</p>"
                + "<p>Meeting Office Planner</p>";

        helper.setSubject(subject);
        helper.setText(content, true);

        mailSender.send(message);
    }

    @GetMapping(path = "/reset_password")
    public String getResetPasswordForm(@Param(value = "token") String token, Model model){
        User user = userService.getUserByToken(token);

        if (user == null){
            model.addAttribute("message", "Invalid Token");
        }

        model.addAttribute("token", token);

        return "reset_password";
    }

    @PostMapping("/reset_password")
    public String resetPassword(HttpServletRequest request, Model model){
        String token = request.getParameter("token");
        String password = request.getParameter("password");

        User user = userService.getUserByToken(token);

        if (user == null){
            model.addAttribute("messageErr", "Invalid Token");
        }
        else {
            userService.updatePassword(user, password);
            model.addAttribute("message", "You have successfully changed your password");
        }

        model.addAttribute("token", token);

        return "login";
    }


    @GetMapping(path = "/set_password")
    public String getSetFirstTimePasswordForm(@Param(value = "token") String token, Model model){
        User user = userService.getUserSetPasswordByToken(token);

        if (user == null){
            model.addAttribute("message", "User doesn't exist");
        }

        model.addAttribute("token", token);

        return "first_time_password";
    }

    @PostMapping("/set_password")
    public String setFirstTimePassword(HttpServletRequest request, Model model){

        String token = request.getParameter("token");
        String password = request.getParameter("password");

        User user = userService.getUserSetPasswordByToken(token);

        if (user == null){
            model.addAttribute("messageErr", "Invalid Token");
        }
        else {

            userService.setFirstTimePassword(user, password);
            model.addAttribute("message", "You have successfully set your password");

        }

        return "login";
    }

}