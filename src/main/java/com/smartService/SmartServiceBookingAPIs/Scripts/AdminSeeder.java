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

import static com.smartService.SmartServiceBookingAPIs.Exception.ErrorsExceptionFactory.notFound;

@Slf4j
@Component
@RequiredArgsConstructor
@Order(2)
public class AdminSeeder implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public void run(String... args) throws Exception {
        log.info("Seeding...");

        seedAdmin();

        log.info("Seeding completed.");
    }

    private void seedAdmin() {
        String adminEmail = "admin@gmail.com";

        if (userRepository.existsByEmail(adminEmail)) {
            log.info("Admin already exist! Skipped seeding.");
            return;
        }

        Roles adminRole = roleRepository.findByName("admin")
                .orElseThrow(() -> notFound("Role not found."));

        Users admin = new Users();
                admin.setFullname("Admin user");
                admin.setUsername("admin123");
                admin.setPhone("0889966553");
                admin.setEmail(adminEmail);
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setRoles(Set.of(adminRole));


        userRepository.save(admin);

        log.info("Admin seed successfully.");
    }
}
