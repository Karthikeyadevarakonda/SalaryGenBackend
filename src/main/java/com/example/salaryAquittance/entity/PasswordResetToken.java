package com.example.salaryAquittance.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "password_reset_tokens")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PasswordResetToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String otp;

    private LocalDateTime expiry;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private AppUser user;

}
