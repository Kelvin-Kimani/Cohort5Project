package com.tracom.cohort5project.Security.Service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

@Component
public class CustomSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                        Authentication authentication) throws IOException, ServletException {

        String redirectUrl = null;

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        for (GrantedAuthority grantedAuthority : authorities) {
            System.out.println("Role: " + grantedAuthority.getAuthority());

            if (grantedAuthority.getAuthority().equals("Admin")) {
                redirectUrl = "/admin/dashboard";
                break;
            }

            else if (grantedAuthority.getAuthority().equals("Organization Officer")) {
                redirectUrl = "/officer/dashboard";
                break;
            }

            else if (grantedAuthority.getAuthority().equals("User")) {
                redirectUrl = "/user/dashboard";
                break;
            }
        }

        System.out.println("redirectUrl " + redirectUrl);
        if (redirectUrl == null) {
            throw new IllegalStateException();
        }

        new DefaultRedirectStrategy().sendRedirect(httpServletRequest, httpServletResponse, redirectUrl);

    }
}
