package com.tracom.cohort5project.Security;

import com.tracom.cohort5project.Security.Service.CustomFailureHandler;
import com.tracom.cohort5project.Security.Service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final AuthenticationSuccessHandler successHandler;

    private final CustomFailureHandler failureHandler;

    public WebSecurityConfig(AuthenticationSuccessHandler successHandler, CustomFailureHandler failureHandler) {
        this.successHandler = successHandler;
        this.failureHandler = failureHandler;
    }

    //Load user from custom user details
    @Bean
    @Override
    public UserDetailsService userDetailsService() {
        return new CustomUserDetailsService();
    }

    //Encrypt password(Fast and strong)
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //Authenticate the username and password provided by the database
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();

        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(bCryptPasswordEncoder());

        return authenticationProvider;
    }

    /*Note: to configure spring security we need to override several methods,
    Configure(HTTpSecurity, AuthenticationManagerBuilder and in it set
    authentication provider for the auth object of the authenticationProviderMethod)
   */

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //Configure authenticationProvider object
        auth.authenticationProvider(authenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //Configure the login and log out for the application
        http.authorizeRequests()

                //list all the users registered on the website, there we use antmatchers to match with the url
                .antMatchers("/css/**", "/js/**", "/images/**", "/", "/login", "/register", "/forgot_password", "/reset_password", "/set_password").permitAll()
                .antMatchers("/admin/**").hasAuthority("Admin")
                .antMatchers("/officer/**").hasAuthority("Organization Officer")
                .antMatchers("/user/**").hasAuthority("User")
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .permitAll()
                .successHandler(successHandler)
                .failureHandler(failureHandler)
                .and()
                .logout()
                .logoutSuccessUrl("/login").permitAll();
    }
}
