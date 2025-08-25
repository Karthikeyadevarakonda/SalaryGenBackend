package com.example.salaryAquittance.auth;


import com.example.salaryAquittance.entity.AppUser;
import com.example.salaryAquittance.enums.Role;
import com.example.salaryAquittance.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {
            userRepository.save(AppUser.builder()
                    .username("admin")
                    .email("admin@gmail.com")
                    .password(passwordEncoder.encode("admin123"))
                    .role(Role.ADMIN)
                    .isEnabled(true)
                    .build());
            userRepository.save(AppUser.builder()
                    .username("hr")
                    .email("hr@gmail.com")
                    .password(passwordEncoder.encode("hr123"))
                    .role(Role.HR)
                    .isEnabled(true)
                    .build());
            userRepository.save(AppUser.builder()
                    .username("staff")
                    .email("staff@gmail.com")
                    .password(passwordEncoder.encode("staff123"))
                    .role(Role.STAFF)
                    .isEnabled(true)
                    .build());
        }
    }
}
