package com.smartService.SmartServiceBookingAPIs.Repositories;

import com.smartService.SmartServiceBookingAPIs.Entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Long> {

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    Optional<Users> findByUsername(String username);

    // Add this:
    Optional<Users> findByEmail(String email);
}
