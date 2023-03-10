package com.tracom.cohort5project.Security.Service;

import com.tracom.cohort5project.Entities.User;
import com.tracom.cohort5project.Services.UserService;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

@Component
public class CustomFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Autowired
    private UserService userService;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        String email = request.getParameter("username");
        User user = userService.getUserByEmail(email);

        if (user != null) {
            if (user.isAccountNonLocked()) {
                if (user.getFailedAttempts() < UserService.MAX_FAILED_ATTEMPTS - 1) {

                    userService.increaseFailedAttempts(user);
                    int remainingAttempts = UserService.MAX_FAILED_ATTEMPTS - user.getFailedAttempts() - 1;
                    exception = new BadCredentialsException("Invalid Email Address or Password. You have " + remainingAttempts + " attempts remaining!");
                } else {
                    userService.lock(user);
                    exception = new LockedException("Your account has been locked due to four failed attempts. It will be unlocked after 30 minutes.");
                }
            } else if (!user.isAccountNonLocked()) {
                if (userService.unlock(user)) {
                    exception = new LockedException("Your account has been unlocked. Please try to login again");
                } else {

                    exception = new LockedException("Your account is locked. Wait for it to be unlocked.");
                }
            }
        }

        super.setDefaultFailureUrl("/login?error");
        super.onAuthenticationFailure(request, response, exception);
    }
}
