package com.tracom.cohort5project.Security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoder {
    public static void main(String[] args) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String rawPassword = "JayJay";
        String encodedPassword = passwordEncoder.encode(rawPassword);

        System.out.println(encodedPassword);

    }
}
