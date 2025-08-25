package com.example.salaryAquittance.auth;

import org.springframework.security.core.context.SecurityContextHolder;


public class SecurityUtil {
    public static UserPrincipal getCurrentUser() {
        return (UserPrincipal) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
    }

}
