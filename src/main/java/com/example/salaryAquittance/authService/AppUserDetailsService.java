package com.example.salaryAquittance.authService;

import com.example.salaryAquittance.auth.UserPrincipal;
import com.example.salaryAquittance.entity.AppUser;
import com.example.salaryAquittance.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AppUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        // Prefix with "ROLE_" is handled by SimpleGrantedAuthority convention
        return new UserPrincipal(user);
    }


}
