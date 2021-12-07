package com.sivalabs.devzone.utils;

import java.util.List;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordGenerator {
    public static void main(String[] args) {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        List<String> plainPasswords = List.of("admin", "demo", "siva");
        plainPasswords.forEach(
                pwd -> {
                    String encryptedPwd = encoder.encode(pwd);
                    System.out.println("pwd = " + pwd);
                    System.out.println("encryptedPwd = " + encryptedPwd);
                });
    }
}
