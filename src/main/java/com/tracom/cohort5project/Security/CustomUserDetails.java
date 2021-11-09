package com.tracom.cohort5project.Security;

import com.tracom.cohort5project.Entities.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;
import java.util.stream.Collectors;

public class CustomUserDetails implements UserDetails {

    private User user;
    private List<GrantedAuthority> authorities;

    public CustomUserDetails(User user){
        this.user = user;
        this.authorities = Arrays.stream(user.getUserRole().split(","))
                .map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmployeeEmailAddress();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public String getFullName(){
        return this.user.getEmployeeFirstName() + " " + this.user.getEmployeeLastName();
    }

    //Update Current LoggedIn user
    public void setFirstName(String firstName){
        this.user.setEmployeeFirstName(firstName);
    }
    public void setLastName(String lastName){
        this.user.setEmployeeLastName(lastName);
    }
}
