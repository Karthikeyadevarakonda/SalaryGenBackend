package com.example.salaryAquittance.controller;

import com.example.salaryAquittance.authService.PasswordResetService;
import com.example.salaryAquittance.dto.PasswordResetDto;
import com.example.salaryAquittance.entity.AppUser;
import com.example.salaryAquittance.entity.Staff;
import com.example.salaryAquittance.repository.StaffRepository;
import com.example.salaryAquittance.repository.UserRepository;
import com.example.salaryAquittance.utility.EmailService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/staff/auth")
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;
    private final PasswordResetService passwordResetService;
    private final UserRepository userRepository;






    // 1. Request password reset (send OTP)
    @PostMapping("/forgot-password")
    public String forgotPassword(@RequestParam String email) throws MessagingException {
        passwordResetService.requestPasswordReset(email);
        return "OTP sent ";
    }

    // 2. Reset password using OTP with DTO
    @PostMapping("/reset-password")
    public String resetPassword(@RequestBody PasswordResetDto dto) {
        passwordResetService.resetPassword(dto.getEmail(), dto.getOtp(), dto.getNewPassword());
        return "Password reset successful!";
    }

}
