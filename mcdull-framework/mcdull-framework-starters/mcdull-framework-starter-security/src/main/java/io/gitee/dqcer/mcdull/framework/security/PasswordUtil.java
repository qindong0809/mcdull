package io.gitee.dqcer.mcdull.framework.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordUtil {

    private static final BCryptPasswordEncoder ENCODER = new BCryptPasswordEncoder(12);

    public  static void main(String[] args) {
        String pass = "1111";
        String hasPass = encode(pass);
        System.out.println(hasPass);
        boolean f = matches(pass, hasPass);
        System.out.println(f);
    }

    public static String encode(String rawPassword) {
        return ENCODER.encode(rawPassword);
    }

    public static boolean matches(String rawPassword, String encodedPassword) {
        return ENCODER.matches(rawPassword, encodedPassword);
    }
}
