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
@Order(3)
public class ProviderSeeder implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public void run(String... args) throws Exception {
        log.info("Seeding...");

        seedProvider();

        log.info(("Seeding completed."));
    }

    private void seedProvider() {
        String providerEmail = "provider@gmail.com";

        if (userRepository.existsByEmail(providerEmail)) {
            log.info("Provider already exist! Skipped seeding.");
            return;
        }

        Roles providerRole = roleRepository.findByName("provider")
                .orElseThrow(() -> notFound("Role not found."));

        Users provider = new Users();
        provider.setFullname("Provider user");
        provider.setUsername("provider12");
        provider.setPhone("0886665522");
        provider.setEmail(providerEmail);
        provider.setPassword(passwordEncoder.encode("provider123"));
        provider.setRoles(Set.of(providerRole));

        userRepository.save(provider);

        log.info("Provider seed successfully.");
    }
}
