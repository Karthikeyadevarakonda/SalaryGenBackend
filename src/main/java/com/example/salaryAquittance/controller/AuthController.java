package com.example.salaryAquittance.controller;

import com.example.salaryAquittance.authService.JwtService;
import com.example.salaryAquittance.dto.authDto.Dtos;
import com.example.salaryAquittance.entity.AppUser;
import com.example.salaryAquittance.entity.Staff;
import com.example.salaryAquittance.enums.Role;
import com.example.salaryAquittance.repository.StaffRepository;
import com.example.salaryAquittance.repository.UserRepository;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final StaffRepository staffRepository;

    @Value("${app.jwt.access-minutes}")
    private long accessMinutes;

    @PostMapping("/auth/login")
    public ResponseEntity<Dtos.LoginResponse> login(@Valid @RequestBody Dtos.LoginRequest request, HttpServletResponse response) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );

        AppUser user = userRepository.findByUsername(request.username()).orElseThrow();
        String token = jwtService.generateAccessToken(user.getUsername(), user.getRole());

        ResponseCookie cookie = ResponseCookie.from("jwt",token)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(900)
                .sameSite("None")
                .build();

        response.setHeader("Set-Cookie",cookie.toString());

        Staff staff = staffRepository.findById(user.getId()).orElse(null);

        String userName = (staff==null)?"demo":staff.getName();
        String role = "is"+ user.getRole().name().substring(0, 1).toUpperCase() + user.getRole().name().substring(1).toLowerCase();

        return ResponseEntity.ok(new Dtos.LoginResponse(
                user.getId(),
                token,
                "Bearer",
                userName,
                role,
                accessMinutes * 60
        ));
    }

    // Optional: allow registering users (disable in prod if needed)
    @PostMapping("/admin/auth/register")
    public ResponseEntity<?> register(@Valid @RequestBody Dtos.RegisterRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            return ResponseEntity.badRequest().body("Username already exists");
        }

        AppUser user = AppUser.builder()
                .username(request.username())
                .password(passwordEncoder.encode(request.password()))
                .role(request.role())
                .isEnabled(true)
                .build();
        userRepository.save(user);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/admin/auth/me")
    public Dtos.MeResponse me(@RequestHeader("Authorization") String authHeader) {
        // Frontend can also decode JWT, but this makes role fetching trivial
        String token = authHeader.replace("Bearer ", "");
        var claims = jwtService.parseAndValidate(token).getPayload();
        return new Dtos.MeResponse(claims.getSubject(), Role.valueOf((String) claims.get("role")));
    }
}
