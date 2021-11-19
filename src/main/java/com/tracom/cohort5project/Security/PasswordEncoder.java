package com.tracom.cohort5project.Security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;

public class PasswordEncoder {
    public static void main(String[] args) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String rawPassword = "JayJay";
        String rawPassword1 = "Kelvin@5257";
        String encodedPassword = passwordEncoder.encode(rawPassword);
//        String encodedPassword1 = passwordEncoder.encode(rawPassword1);

        System.out.println(encodedPassword);
        System.out.println(passwordEncoder.matches(rawPassword, encodedPassword));
//        System.out.println(encodedPassword1);
        System.out.println(LocalDate.now());

    }
}