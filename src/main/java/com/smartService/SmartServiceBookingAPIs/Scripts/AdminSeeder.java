package com.smartService.SmartServiceBookingAPIs.Scripts;

import com.smartService.SmartServiceBookingAPIs.Entity.Roles;
import com.smartService.SmartServiceBookingAPIs.Entity.Users;
import com.smartService.SmartServiceBookingAPIs.Repositories.RoleRepository;
import com.smartService.SmartServiceBookingAPIs.Repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;

import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
@Order(2)
public class AdminSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        log.info("Seeding....");

        if (userRepository.findByUsername("admin").isEmpty()) {

            Roles adminRole = roleRepository.findByName("admin")
                    .orElseGet(() -> roleRepository.save(
                            Roles.builder().name("admin").build()
                    ));

            Users admin = Users.builder()
                    .fullname("administrator")
                    .username("admin")
                    .email("admin@gmail.com")
                    .password(passwordEncoder.encode("admin123")) // lazy injected
                    .roles(Set.of(adminRole))
                    .build();

            userRepository.save(admin);
            log.info("admin user created!");
        } else {
            log.info("admin user already exists!");
        }
    }
}
