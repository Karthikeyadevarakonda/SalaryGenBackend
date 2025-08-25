package com.example.salaryAquittance.repository;


import com.example.salaryAquittance.entity.AppUser;
import com.example.salaryAquittance.entity.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findByUser(AppUser user);
    Optional<PasswordResetToken> findByUserAndOtp(AppUser user, String otp);
}
