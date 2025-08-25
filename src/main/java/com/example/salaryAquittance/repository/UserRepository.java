package com.example.salaryAquittance.repository;

import com.example.salaryAquittance.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByUsername(String username);
    boolean existsByUsername(String username);
    Optional<AppUser> findByEmail(String email);
}

