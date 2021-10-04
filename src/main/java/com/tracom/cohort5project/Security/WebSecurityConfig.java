package com.tracom.cohort5project.Security;

import com.tracom.cohort5project.Security.Service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    //Connect user to database for authentication
    @Autowired
    private DataSource dataSource;

    //Load user from custom user details
    @Bean
    public UserDetailsService userDetailsService(){
        return new CustomUserDetailsService();
    }

    //Encrypt password(Fast and strong)
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    //Authenticate the username and password provided by the database
    @Bean
    public DaoAuthenticationProvider authenticationProvider(){
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
                //Authenticated means you can view this page
                .antMatchers("/adminWelcomePage").authenticated()
                .anyRequest().permitAll()

                //Configuring to the login page we used before or provided by spring security
                .and()
                .formLogin()
//                .loginPage("/login")
                    .usernameParameter("emailAddress")
                    //Landing page for the user on successful login
                    .defaultSuccessUrl("/adminWelcomePage")
                    .permitAll()

                //Implementing logout
                .and()
                .logout().logoutSuccessUrl("/").permitAll();
    }
}
