package com.favorapp.api.config.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;

public class PasswordEncoder implements org.springframework.security.crypto.password.PasswordEncoder {

    //Pbkdf2PasswordEncoder passwordEncoder = new Pbkdf2PasswordEncoder("", 100000, 256);
    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    @Override
    public String encode(CharSequence rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
