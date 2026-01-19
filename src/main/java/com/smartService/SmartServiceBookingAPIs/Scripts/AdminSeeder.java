package com.smartService.SmartServiceBookingAPIs.Scripts;

import com.smartService.SmartServiceBookingAPIs.Entity.Roles;
import com.smartService.SmartServiceBookingAPIs.Entity.Users;
import com.smartService.SmartServiceBookingAPIs.Repositories.RoleRepository;
import com.smartService.SmartServiceBookingAPIs.Repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


import java.util.Set;

import static org.springframework.context.annotation.Role.*;

@Component
public class AdminSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminSeeder(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        // Check if admin user already exists
        if (userRepository.findByUsername("admin").isEmpty()) {

            // Check if ADMIN role exists, else create
            Roles adminRole = roleRepository.findByName("admin")
                    .orElseGet(() -> roleRepository.save(
                            Roles.builder().name("admin").build()
                    ));

            // Create admin user
            Users admin = Users.builder()
                    .fullname("administrator")
                    .username("admin")
                    .email("admin@gmail.com")
                    .password(passwordEncoder.encode("admin123")) // encode password
                    .roles(Set.of(adminRole))
                    .build();

            userRepository.save(admin);
            System.out.println("admin user created!");
        } else {
            System.out.println("admin user already exists!");
        }
    }
}
