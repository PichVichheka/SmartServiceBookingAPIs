package com.smartService.SmartServiceBookingAPIs.Services.Impl;

import com.smartService.SmartServiceBookingAPIs.Entity.Users;
import com.smartService.SmartServiceBookingAPIs.Repositories.UserRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

// ============================
// Custom UserDetailsService implementation for loading user-specific data.
// Loads users by email and wraps them in a Spring Security UserDetails object.
// ============================
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    // ============================
    // Loads user by email (used as username in authentication).
    // Spring Security will call this method during authentication.
    // ============================
    @Override
    @NullMarked
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "User not found with email: " + email
                ));

        return new CustomUserDetails(user);
    }

    // ============================
    // Custom UserDetails implementation that wraps the Users entity.
    // Provides user information to Spring Security authentication.
    // ============================
    @Getter
    public static class CustomUserDetails implements UserDetails {

        private final Users user;
        private final Collection<? extends GrantedAuthority> authorities;

        public CustomUserDetails(Users user) {
            this.user = user;
            this.authorities = user.getRoles().stream()
                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName().toUpperCase()))
                    .collect(Collectors.toSet());
        }

        @Override
        @NullMarked
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return authorities;
        }

        @Override
        public String getPassword() {
            return user.getPassword();
        }

        @Override
        @NullMarked
        public String getUsername() {
            // Return email as username for authentication
            return user.getEmail();
        }

        @Override
        public boolean isAccountNonExpired() {
            return true;
        }

        @Override
        public boolean isAccountNonLocked() {
            return true;
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return true;
        }

        @Override
        public boolean isEnabled() {
            return true;
        }
    }
}