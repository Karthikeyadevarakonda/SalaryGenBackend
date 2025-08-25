package com.example.salaryAquittance.auth;

import com.example.salaryAquittance.authService.AppUserDetailsService;
import com.example.salaryAquittance.authService.JwtService;
import com.example.salaryAquittance.entity.AppUser;
import com.example.salaryAquittance.repository.UserRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final AppUserDetailsService appUserDetailsService;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // If already authenticated, skip parsing
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = null;

        // 1. Try Authorization header first
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(header) && header.startsWith("Bearer ")) {
            token = header.substring(7);
        }

        // 2. If not found, check cookies
        if (token == null) {
            token = extractJwtFromCookies(request);
        }

        // 3. Validate & authenticate
        if (token != null) {
            try {
                var jws = jwtService.parseAndValidate(token);
                Claims claims = jws.getPayload();

                String username = claims.getSubject();
                if (username != null) {
                    AppUser user = userRepository.findByUsername(username)
                            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
                    UserPrincipal userPrincipal = new UserPrincipal(user);
                    // Option A: Take authorities from DB (preferred in most real systems)
                    var auth = new UsernamePasswordAuthenticationToken(
                            userPrincipal,
                            null,
                            userPrincipal.getAuthorities()
                    );

                    // Option B: If you must rely on claim "role" instead:
                    // String role = (String) claims.get("role");
                    // var auth = new UsernamePasswordAuthenticationToken(
                    //        userDetails, null, List.of(new SimpleGrantedAuthority("ROLE_" + role))
                    // );

                    SecurityContextHolder.getContext().setAuthentication(auth);
                }

            } catch (Exception e) {
                // Invalid/expired token -> just don’t authenticate
                SecurityContextHolder.clearContext();
            }
        }

        filterChain.doFilter(request, response);
    }

    private String extractJwtFromCookies(HttpServletRequest request) {
        if (request.getCookies() == null) return null;
        for (Cookie cookie : request.getCookies()) {
            if ("jwt".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }
}
